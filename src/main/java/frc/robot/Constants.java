/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    // USB
	public static final int driveController = 0;
	public static final int operatorController = 1;
	public static final int driverJoyLeft = 2;
	public static final int driverJoyRight = 3;

	// CAN
	public static final int leftDriveMotor = 1;
	public static final int leftDriveSPX1 = 1;
	public static final int leftDriveSPX2 = 3;
	public static final int rightDriveMotor = 2;
	public static final int rightDriveSPX1 = 2;
    public static final int rightDriveSPX2 = 4;
    public static final int intakeMotor = 5;
    public static final int conveyorMotor = 7;
    public static final int feederMotor = 6;
    public static final int shooterLeft = 5;
    public static final int shooterRight = 4;
	public static final int spinner = 3;
	
    //PCM
	public static final int spinnerUp = 0;
	public static final int spinnerDown = 1;
	public static final int slideIn = 2;
	public static final int slideOut = 3;
	public static final int driveHigh = 7;
	public static final int driveLow = 6;
	
    //Digital Imports
}
