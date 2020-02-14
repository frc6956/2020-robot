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
    //USB
    public static final int driverJoyLeft = 0;
    public static final int driverJoyRight = 1;
    public static final int operatorController = 2;
    public static final int driveController = 3;

    //CAN
    public static final int spinner = 7;
    //PWM
	public static final int doubleSolenoidA = 1;
	public static final int doubleSolenoidB = 0;
    //Digital Imports
}
