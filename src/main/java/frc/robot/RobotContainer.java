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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  //Subsystems
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final Intake m_intake = new Intake();
  private final Conveyor m_conveyor = new Conveyor();
  private final Feeder m_feeder = new Feeder();
  private final GyroPigeon m_gyro = new GyroPigeon();
  private final LinearSlide m_slide = new LinearSlide();
  private final ColorSpinner m_spinner = new ColorSpinner();

  //OI Devices
  private final Joystick m_driverLeftJoystick = new Joystick(Constants.driverJoyLeft);
  private final Joystick m_driverRightJoystick = new Joystick(Constants.driverJoyRight);
  private final XboxController m_operatorController = new XboxController(Constants.operatorController);

  //Commands
    //Driver
  private final Command m_splitArcadeJoystick = new RunCommand(
    () -> m_drivetrain.arcadeDrive(-m_driverLeftJoystick.getY(), m_driverRightJoystick.getX()), m_drivetrain);

  private final Command m_driverSwitchHigh = new RunCommand(
    () -> m_drivetrain.switchGear(), m_drivetrain);
  
  private final Command m_driverSwitchLow = new RunCommand(
    () -> m_drivetrain.switchGear(), m_drivetrain);

  private final Command m_invertDrive = new RunCommand(
    () -> m_drivetrain.reverse(!m_drivetrain.isReversed()), m_drivetrain);

    //Operator
  private final Command m_TeleopIntake  = new RunCommand(
    () -> m_intake.setIntakeSpeed(-m_operatorController.getY(Hand.kLeft)), m_intake);

  private final Command m_TeleopConveyor = new RunCommand(
    () -> m_conveyor.setConveyorSpeed(m_operatorController.getY(Hand.kLeft)), m_conveyor);

  private final Command m_TeleopFeeder = new RunCommand(
    () -> m_feeder.setFeedSpeed(m_operatorController.getX(Hand.kRight)), m_feeder);

  private final Command m_TeleopSlideIn = new RunCommand(
    () -> m_slide.actuateIn(), m_slide);

  private final Command m_TeleopSlideOut = new RunCommand(
    () -> m_slide.actuateOut(), m_slide);

    private final Command m_SpinnerUp = new RunCommand(
      () -> m_slide.actuateIn(), m_slide);
  
    private final Command m_SpinnerDown = new RunCommand(
      () -> m_slide.actuateOut(), m_slide);

  private final SequentialCommandGroup m_AutoGrab = new SequentialCommandGroup(new Slide(m_slide),  new WaitCommand(2), new Slide(m_slide) );




  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Default Commands
    m_drivetrain.setDefaultCommand(m_splitArcadeJoystick);
    m_intake.setDefaultCommand(m_TeleopIntake);
    m_conveyor.setDefaultCommand(m_TeleopConveyor);
    m_feeder.setDefaultCommand(m_TeleopFeeder);

		CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);
    
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(m_driverLeftJoystick, 5).whenPressed(m_driverSwitchHigh);
    new JoystickButton(m_driverRightJoystick, 6).whenPressed(m_driverSwitchLow);
    new JoystickButton(m_driverRightJoystick, 1).whenPressed(m_invertDrive);
    new JoystickButton(m_operatorController, XboxController.Button.kB.value).whenPressed(m_TeleopSlideOut);
    new JoystickButton(m_operatorController, XboxController.Button.kA.value).whenPressed(m_TeleopSlideIn);
    new JoystickButton(m_operatorController, XboxController.Button.kX.value).whenPressed(m_SpinnerDown);
    new JoystickButton(m_operatorController, XboxController.Button.kY.value).whenPressed(m_SpinnerUp);
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new SequentialCommandGroup(new DriveDistancePID(m_drivetrain, m_gyro, 120), new TurnAnglePID(m_drivetrain, m_gyro, 180) );
  }
}
