/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.LinearSlide;

public class Slide extends CommandBase {
  /**
   * Creates a new Slide.
   */

  private final LinearSlide mSlide;
  private DoubleSolenoid m_slideSolenoid = new DoubleSolenoid(Constants.doubleSolenoidC, Constants.doubleSolenoidD);

  public Slide(LinearSlide slide) {
    // Use addRequirements() here to declare subsystem dependencies.
    mSlide = slide;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_slideSolenoid.get() != DoubleSolenoid.Value.kReverse) {
      m_slideSolenoid.set(DoubleSolenoid.Value.kReverse);
  }
  else if(m_slideSolenoid.get() != DoubleSolenoid.Value.kForward) {
      m_slideSolenoid.set(DoubleSolenoid.Value.kForward);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
