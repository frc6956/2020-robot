/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Intake;
/**
 * Creates a new IntakeConveyor.
 */

public class IntakeConveyor extends CommandBase {

  private Intake mIntake;
  private Conveyor mConveyor;
  private double conSpeed;
  private double inSpeed;

  public IntakeConveyor(final Intake intake, final Conveyor conveyor, final double conveySpeed, final double intakeSpeed) {
    mIntake = intake;
    mConveyor = conveyor;
    conSpeed = conveySpeed;
    inSpeed = intakeSpeed;
    addRequirements(mIntake);
    addRequirements(mConveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    mIntake.setIntakeSpeed(inSpeed);
    mConveyor.setConveyorSpeed(conSpeed);
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
