/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot is declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  //Subsystems
  private final PowerDistributionPanel m_pdp = new PowerDistributionPanel();
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final Intake m_intake = new Intake();
  private final Conveyor m_conveyor = new Conveyor(m_pdp);
  private final Feeder m_feeder = new Feeder();
  private final GyroPigeon m_gyro = new GyroPigeon();
  private final LinearSlide m_slide = new LinearSlide();
  private final ColorSpinner m_spinner = new ColorSpinner();
  private final Shooter m_shooter = new Shooter();

  //OI Devices
  private final Joystick m_driverLeftJoystick = new Joystick(Constants.USB.driverJoyLeft);
  private final Joystick m_driverRightJoystick = new Joystick(Constants.USB.driverJoyRight);
  private final XboxController m_operatorController = new XboxController(Constants.USB.operatorController);
  
  SendableChooser<SequentialCommandGroup> m_chooser = new SendableChooser<>();

  //Commands
    //Driver
  private final Command m_splitArcadeJoystick = new RunCommand(
    () -> m_drivetrain.arcadeDrive(-m_driverRightJoystick.getY(), m_driverLeftJoystick.getX()), m_drivetrain);

  private final Command m_tankJoystick = new RunCommand(
    () -> m_drivetrain.tankDrive(-m_driverLeftJoystick.getY(), -m_driverRightJoystick.getY()), m_drivetrain);

  private final Command m_driverSwitchHigh = new InstantCommand(
    () -> m_drivetrain.highGear());
  
  private final Command m_driverSwitchLow = new InstantCommand(
    () -> m_drivetrain.lowGear());

  private final Command m_invertDrive = new InstantCommand(
    () -> m_drivetrain.reverse(!m_drivetrain.isReversed()));

     //Operator
  private final Command m_TeleopIntake  = new RunCommand(
    () -> m_intake.setIntakeSpeed(-m_operatorController.getY(Hand.kLeft)), m_intake);

  private final Command m_TeleopConveyor = new RunCommand(
    () -> m_conveyor.setConveyorSpeed(m_operatorController.getY(Hand.kLeft)), m_conveyor);

  private final Command m_TeleopFeeder = new RunCommand(
    () -> m_feeder.setFeedSpeed(m_operatorController.getY(Hand.kRight)), m_feeder);

  private final Command m_TeleopShooter = new RunCommand(
    () -> m_shooter.setShooterSpeed(m_operatorController.getTriggerAxis(Hand.kRight)), m_shooter);

  private final Command m_TeleopSlideIn = new RunCommand(
    () -> m_slide.actuateIn(), m_slide);

  private final Command m_TeleopSlideOut = new RunCommand(
    () -> m_slide.actuateOut(), m_slide);

  private final Command m_SpinnerUp = new RunCommand(
    () -> m_spinner.up(), m_spinner);

  private final Command m_SpinnerDown = new RunCommand(
    () -> m_spinner.down(), m_spinner);

  private final Command m_SpinnerSpinR = new RunCommand(
    () -> m_spinner.setRPM(640), m_spinner);

  private final Command m_SpinnerSpinL = new RunCommand(
    () -> m_spinner.setRPM(-640), m_spinner);
  
  private final Command m_SpinnerStop = new RunCommand(
    () -> m_spinner.setWheelSpeed(0), m_spinner);

  private final SequentialCommandGroup m_AutoGrab = new SequentialCommandGroup(new Slide(m_slide),  new WaitCommand(2), new Slide(m_slide) );

  //Auton
  private final Command autonShoot = new RunCommand(
    () -> m_shooter.setShooterSpeed(1), m_shooter);



  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Default Commands
    m_drivetrain.setDefaultCommand(m_splitArcadeJoystick);
    m_intake.setDefaultCommand(m_TeleopIntake);
    m_conveyor.setDefaultCommand(m_TeleopConveyor);
    m_feeder.setDefaultCommand(m_TeleopFeeder);
    m_shooter.setDefaultCommand(m_TeleopShooter);

    CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);
    SmartDashboard.putData(m_pdp);
    
    configureButtonBindings();

    m_chooser.addOption("Do nothing", null);
    m_chooser.setDefaultOption("Drive", new SequentialCommandGroup(new DriveDistance(m_drivetrain, 18.0)));
    
    m_chooser.addOption("Shoot n' Drive Forwards", new SequentialCommandGroup(new Shoot(m_shooter, 1.0, 3), new Feed(m_feeder, 1.0), new WaitCommand(4), 
        new IntakeConveyor(m_intake, m_conveyor, 1.0, 1.0), new WaitCommand(3), new DriveDistance(m_drivetrain, 18)  ) );
    m_chooser.addOption("Shoot n' Drive Backwards", new SequentialCommandGroup(new Shoot(m_shooter, 1.0, 3), new Feed(m_feeder, 1.0), new WaitCommand(4), 
        new IntakeConveyor(m_intake, m_conveyor, 1.0, 1.0), new WaitCommand(3), new DriveDistance(m_drivetrain, -18)  ) );
    m_chooser.addOption("Drive & Shoot", new SequentialCommandGroup(new DriveDistance(m_drivetrain, 18), new Shoot(m_shooter, 1.0, 3),
        new FeedShoot(m_shooter, m_feeder, m_conveyor, 1, 1, 1, 5)) );
    m_chooser.addOption("Shoot & Drive", new SequentialCommandGroup(new Shoot(m_shooter, 1.0, 3), 
    new FeedShoot(m_shooter, m_feeder, m_conveyor, 1, 1, 1, 5), new DriveDistance(m_drivetrain, 18) ) );

    //m_chooser.addOption("Drive n' Turn", new SequentialCommandGroup(new DriveDistance(m_drivetrain, 120), new TurnAnglePID(m_drivetrain, m_gyro, 180) ));
    SmartDashboard.putData("Auto mode", m_chooser);
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

//Driver Configs
    //new JoystickButton(m_driverLeftJoystick, 5).whenPressed(m_driverSwitchHigh);
    //new JoystickButton(m_driverRightJoystick, 6).whenPressed(m_driverSwitchLow);
    new JoystickButton(m_driverRightJoystick, 6).whenPressed(m_driverSwitchHigh);
    
    new JoystickButton(m_driverRightJoystick, 6).whenReleased(m_driverSwitchLow);
    new JoystickButton(m_driverRightJoystick, 1).whenPressed(m_invertDrive);
    new JoystickButton(m_driverLeftJoystick, 1).whenPressed(m_TeleopSlideIn);

//Operator Configs
    new JoystickButton(m_operatorController, XboxController.Button.kY.value).whenPressed(m_SpinnerUp);
    new JoystickButton(m_operatorController, XboxController.Button.kX.value).whenPressed(m_SpinnerDown);
    new JoystickButton(m_operatorController, XboxController.Button.kA.value).whenPressed(m_TeleopSlideIn);
    new JoystickButton(m_operatorController, XboxController.Button.kB.value).whenPressed(m_TeleopSlideOut);

    new JoystickButton(m_operatorController, XboxController.Button.kBumperLeft.value).whileHeld(m_SpinnerSpinL);
    new JoystickButton(m_operatorController, XboxController.Button.kBumperLeft.value).whenReleased(m_SpinnerStop);
    new JoystickButton(m_operatorController, XboxController.Button.kBumperRight.value).whileHeld(m_SpinnerSpinR);
    new JoystickButton(m_operatorController, XboxController.Button.kBumperRight.value).whenReleased(m_SpinnerStop);
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    //return new SequentialCommandGroup(new DriveDistancePID(m_drivetrain, m_gyro, 120), new TurnAnglePID(m_drivetrain, m_gyro, 180) );
    return m_chooser.getSelected();
  }
}
