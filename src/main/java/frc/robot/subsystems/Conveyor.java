/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


/**
 * Inside belts of the robot
 */
public class Conveyor extends SubsystemBase {
  
  private WPI_VictorSPX m_motor;

  public Conveyor() {
    m_motor = new WPI_VictorSPX(Constants.CAN.conveyorMotor);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Sets the speed of the conveyor by percentage of bus voltage
   */
  public void setConveyorSpeed(double speed) {
    m_motor.set(-speed);
  }

}
