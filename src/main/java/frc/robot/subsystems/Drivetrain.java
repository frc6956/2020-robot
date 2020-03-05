/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * Creates a new Drivetrain
 */
public class Drivetrain extends SubsystemBase {
  
  DoubleSolenoid doubleSolenoid = new DoubleSolenoid(Constants.PCM.driveLow, Constants.PCM.driveHigh);
  private final DifferentialDrive m_drive;

  private final WPI_TalonSRX m_leftSRX;
  private final WPI_TalonSRX m_rightSRX;

  private final WPI_VictorSPX m_leftSPX1;
  private final WPI_VictorSPX m_rightSPX1;

  private final WPI_VictorSPX m_leftSPX2;
  private final WPI_VictorSPX m_rightSPX2;

  private double zeroDistance = 0;
  private boolean reverse = false;
  private boolean low = true;
  private final double ticksPerInch = 4096 / (8 * Math.PI);

  private double speedAverage = 0;
  private double speedDifference = 0;

  public Drivetrain() {
    m_leftSRX = new WPI_TalonSRX(Constants.CAN.leftDriveMotor);
    m_leftSPX1 = new WPI_VictorSPX(Constants.CAN.leftDriveSPX1);
    m_leftSPX2 = new WPI_VictorSPX(Constants.CAN.leftDriveSPX2);
    
    m_rightSRX = new WPI_TalonSRX(Constants.CAN.rightDriveMotor);
    m_rightSPX1 = new WPI_VictorSPX(Constants.CAN.rightDriveSPX1);
    m_rightSPX2 = new WPI_VictorSPX(Constants.CAN.rightDriveSPX2);

    m_drive = new DifferentialDrive(m_leftSRX, m_rightSRX);

    m_leftSPX1.follow(m_leftSRX);
    m_leftSPX2.follow(m_leftSRX);
    m_rightSPX1.follow(m_rightSRX);
    m_rightSPX2.follow(m_rightSRX);

    m_leftSRX.setInverted(InvertType.InvertMotorOutput);
    m_leftSPX1.setInverted(InvertType.FollowMaster);
    m_leftSPX2.setInverted(InvertType.FollowMaster);
    m_rightSRX.setInverted(InvertType.None);
    m_rightSPX1.setInverted(InvertType.FollowMaster);
    m_rightSPX2.setInverted(InvertType.FollowMaster);

    m_drive.setRightSideInverted(false);

    m_leftSRX.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    m_leftSRX.setSensorPhase(true);
    m_rightSRX.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    displayDistance();
  }

  /**
   * Displays the total distance traveled
   */
  public void displayDistance() {
      SmartDashboard.putNumber("Distance Traveled: ", getDistanceTravelled());
  }

  public void tankDrive(double left, double right) {
    tankDrive(left, right, true);
  }

  public void tankDrive(final double left, final double right, boolean squareInputs) {
    if (reverse) {
      m_drive.tankDrive(-right, -left, squareInputs);
    } else {
      m_drive.tankDrive(left, right, squareInputs);
    }
  }

  /**
   * Sets Arcade Drive speed and rotation
   * @param speed Speed
   * @param rotation Rotation
   */
  public void arcadeDrive(double speed, double rotation) {
    if (reverse) {
      speed = -speed;
    } else {
      speed = speed;
    }
    SmartDashboard.putNumber("speed", speed);
    SmartDashboard.putNumber("rotation", rotation);
    m_drive.arcadeDrive(speed, rotation);
  }

  /**
   * Gets the distance travelled 
   * @return Distance travelled since reset
   */
  public double getDistanceTravelled() {
    return getRawDistanceTravelled() - zeroDistance;
  }

  /**
   * Resets the distance travelled
   */
  public void resetDistanceTravelled() {
    zeroDistance = getRawDistanceTravelled();
  }

  /**
   * Reverses the drivetrain
   * @param reverse Reverse the drivetrain
   */
  public void reverse(final boolean reverse) {
    this.reverse = reverse;
  }

  /**
   * Checks if the drivetrain is reversed
   * @return True if the drivetrain is reversed
   */
  public boolean isReversed() {
    return reverse;
  }
  /**
   * Gets the total distance travelled
   * @return Raw non-zeroed distance
   */
  protected double getRawDistanceTravelled() {
    double total = m_leftSRX.getSelectedSensorPosition(0) / ticksPerInch;
    total += m_rightSRX.getSelectedSensorPosition(0) / ticksPerInch;
    return (total / 2);
  }

  /**
   * Sets the robot to low gear for a 2 speed gearbox
   */
  public void lowGear() {
		if (doubleSolenoid.get() != DoubleSolenoid.Value.kForward) {
			doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    low = true;
  }
  
  /**
   * Sets the robot to high gear for a 2 speed gearbox
   */
  public void highGear() {
		if (doubleSolenoid.get() != DoubleSolenoid.Value.kReverse) {
			doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    low = false;
  }

  /**
   * Automatically switches gears of the drivetrain based on Left side and Right side Velocity
   */
  public void switchGear() {
    speedAverage = m_leftSRX.getSelectedSensorVelocity() * 10 / ticksPerInch;
    speedAverage += m_rightSRX.getSelectedSensorVelocity() * 10 / ticksPerInch;
    speedAverage /= 2;

    speedDifference = Math.abs(m_leftSRX.getSelectedSensorVelocity() - m_rightSRX.getSelectedSensorVelocity() );

    if(speedAverage > 10 && low==true) {
      highGear();
    } 

    if(speedAverage < 7 && low==false) {
      lowGear();
    }

  }

}
