/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Feeder;

  /**
   * Creates a new FeedShoot.
   */
public class FeedShoot extends CommandBase {
  private final Shooter mShooter;
  private final Feeder mFeeder;
  private final double shootSpeed;
  private final double feedSpeed;

  public FeedShoot(final Shooter shooter, final Feeder feeder, final double shooterSpeed, final double feederSpeed) {
    mShooter = shooter;
    mFeeder = feeder;
    shootSpeed = shooterSpeed;
    feedSpeed = feederSpeed;
    addRequirements(mShooter);
    addRequirements(mFeeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    mShooter.setShooterSpeed(shootSpeed);
    mFeeder.setFeedSpeed(feedSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    mShooter.setShooterRPM(shootSpeed);
    mFeeder.setFeedSpeed(feedSpeed);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs( (mShooter.getLeftShooterRPM()+mShooter.getRightShooterRPM() )/2-shootSpeed)<30;
  }
}
