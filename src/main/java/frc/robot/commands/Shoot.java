/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.*;
  /**
   * Creates a new Shooter.
   */
public class Shoot extends CommandBase {

  private Shooter mshooter;
  private double shooterSpeed;
  private final double timeout;
  private final Timer timer;

  public Shoot(final Shooter shooter, final double speed, final double timeout) {
    mshooter = shooter;
    shooterSpeed = speed;
    this.timeout = timeout;
    timer = new Timer();
    addRequirements(mshooter);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    mshooter.setShooterSpeed(shooterSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    mshooter.setShooterSpeed(shooterSpeed);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasPeriodPassed(timeout);
  }
}
