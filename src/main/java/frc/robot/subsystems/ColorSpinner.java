/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import com.revrobotics.*;
import frc.robot.Constants;

public class ColorSpinner extends SubsystemBase {
  /**
   * Creates a new Drivetrain.
   */

   private Talon spinnerMotor = new Talon(Constants.spinner);
   private ColorSensorV3 clrSensor = new ColorSensorV3(I2C.Port.kOnboard);
   private ColorMatch clrMatch = new ColorMatch();
   private Color matchedColor;

   DoubleSolenoid doubleSolenoid = new DoubleSolenoid(Constants.doubleSolenoidA, Constants.doubleSolenoidB);
   

  public ColorSpinner() {
    clrMatch.addColorMatch(Color.kAqua);
    clrMatch.addColorMatch(Color.kRed);
    clrMatch.addColorMatch(Color.kYellow);
    clrMatch.addColorMatch(Color.kLime);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void displayRGB() {
    SmartDashboard.putNumber("Red:", clrSensor.getRed());
    SmartDashboard.putNumber("Green:", clrSensor.getGreen());
    SmartDashboard.putNumber("Blue:", clrSensor.getBlue());
  }

  public void displayColor() {

    matchedColor = clrMatch.matchClosestColor(clrSensor.getColor()).color;
    String matchedClr = "no close color found";
    if(matchedColor.equals(Color.kLime)){
      matchedClr = "Green";
    }
    else if(matchedColor.equals(Color.kAqua)){
      matchedClr = "Aqua";
    }
    else if(matchedColor.equals(Color.kYellow)){
      matchedClr = "Yellow";
    }
    else if(matchedColor.equals(Color.kRed)){
      matchedClr = "Red";
    }
    SmartDashboard.putString("Color", matchedClr);
    SmartDashboard.putNumber("Color Confidnece", clrMatch.matchClosestColor(clrSensor.getColor()).confidence);
  }

  public void up() {
    if (doubleSolenoid.get() != DoubleSolenoid.Value.kReverse) {
      doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  }

  public void down() {
    if (doubleSolenoid.get() != DoubleSolenoid.Value.kForward) {
      doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    }
  }

  public void setWheelSpeed(double speed) {
    spinnerMotor.setSpeed(speed);
  }
}
