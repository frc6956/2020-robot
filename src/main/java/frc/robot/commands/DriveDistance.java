/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.GyroPigeon;

  /**
   * Drives the robot forward based in inches and corrects for drivetrain slide
   */

public class DriveDistance extends CommandBase {

  private Drivetrain m_drivetrain;
  private GyroPigeon m_gyropigeon;
  private double m_requestedDistance;
  private boolean turning = false;
  private double KPcorrectionFactor = 0.05;
  private double KIcorrectionFactor = 0.0;
  private double KDcorrectionFactor = 0.0;
  private double targetAngle = 0;
  private double correction;


  public DriveDistance(Drivetrain drivetrain, GyroPigeon pigeon, double distance) {
    m_drivetrain = drivetrain;
    m_gyropigeon = pigeon;
    m_requestedDistance = distance;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drivetrain.resetDistanceTravelled();
    m_gyropigeon.reset();
    KPcorrectionFactor = SmartDashboard.getNumber("KP: ", KPcorrectionFactor);
    KIcorrectionFactor = SmartDashboard.getNumber("KI: ", KIcorrectionFactor);
    KDcorrectionFactor = SmartDashboard.getNumber("KD: ", KDcorrectionFactor);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      //correction using a PID loop
      correction = m_gyropigeon.getAngle()*KPcorrectionFactor;
      m_drivetrain.tankDrive(.5-correction, .5+correction);

    //correction without a PID loop
   /* if(m_gyropigeon.getAngle()>3 || (turning && m_gyropigeon.getAngle()>.5)) {
      //this will turn the robot right   
      m_drivetrain.tankDrive(0.1, 0.3, false);
      turning = true;   
    }
    else if(m_gyropigeon.getAngle()<-3 || (turning && m_gyropigeon.getAngle()<-.5)) {
      //this will turn the robot left   
      m_drivetrain.tankDrive(0.3, 0.1, false);
      turning = true;   
    }
    else {
      m_drivetrain.tankDrive(0.4, 0.5);
      turning = false;
    }*/
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.tankDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_drivetrain.getDistanceTravelled() > m_requestedDistance);
  }
}