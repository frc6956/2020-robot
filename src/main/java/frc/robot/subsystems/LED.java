/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class LED extends SubsystemBase {

  private SerialPort serial;
  private DriverStation ds = DriverStation.getInstance();
  private boolean connected;
  private int location, red, green, blue;
  private AddressableLED m_led = new AddressableLED(7);
  private AddressableLEDBuffer buffer = new AddressableLEDBuffer(19);
  private Timer timer = new Timer();
  private int[][] colors = {{255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {255, 255, 0}, {255, 255, 0}, {255, 255, 0}, {255, 255, 0}, {255, 255, 0}, {0, 255, 0}, {0, 255, 0}, {0, 255, 0}, {0, 255, 0}, {0, 255, 0}, {0, 0, 255}, {0, 0, 255}, {0, 0, 255}, {0, 0, 255}, {0, 0, 255}};

  private int counter = 0;

  public LED() {
    m_led.setLength(buffer.getLength());
    m_led.setData(buffer);
    m_led.start();
    timer.start();
  }

  @Override
  public void periodic() {
    if(ds.isDSAttached()) {
      if(ds.isOperatorControl() && ds.isEnabled()) {
        teleop();
      } else if (ds.isAutonomous() && ds.isEnabled()) {
        autonomous();
      } else if (ds.isEStopped()) {
        setAllRGB(255, 0, 0);
      } else {
        disabled();
      }
    } else {
      clear();
    }
  }
  

  private void setRGB(int index, int r, int g, int b) {
    //red is actually blue
    //green is actually red
    //blue is actually green
    buffer.setRGB(index, b, r, g);
  }

  private void update() {
    m_led.setData(buffer);
  }

  private void setAllRGB(int r, int g, int b) {
    for (int i = 0; i < buffer.getLength(); i++) {
      setRGB(i, r, g, b);
    }
    update();
  }

  private void clear() {
    for(int i = 0; i < buffer.getLength(); i++) {
      setRGB(i, 0, 0, 0);
    }
    update();
  }

  private void teleop() {
    setRGB(counter, 0, 0, 0);
    counter++;
    counter %= buffer.getLength();
    setRGB(counter, 0, 255, 0);
    update();
  }

  private void spinningWheel()
  {
    if(timer.hasPeriodPassed(0.01)) {
      for(int i = 0; i < 19; i++) {
        if(counter + i < 19) {
          setRGB(i, colors[counter + i][0], colors[counter + i][1], colors[counter + i][2]);
        }
        else if(counter + i != 19) {
          setRGB(i, colors[(counter + i - 20)][0], colors[(counter + i - 20)][1], colors[(counter + i - 20)][2]);
        }
      }

      counter++;
      counter %= 20;
    }
  }

  private void autonomous() {
    /*setRGB(counter, 0, 0, 0);
    counter++;
    counter %= buffer.getLength();
    if(ds.getAlliance().equals(DriverStation.Alliance.Blue)) {
      setRGB(counter, 0, 0, 255);
    } else {
      setRGB(counter, 255, 0, 0);
    }
    update();*/
    setRGB(counter, 0, 0, 0);
    counter++;
    counter %= buffer.getLength();
    if(counter%2 == 0)
      setRGB(counter, 255, 255, 0);
    else
      setRGB(counter, 0, 255, 0);
    update();
  }

  private void disabled() {
    setRGB(counter, 0, 0, 0);
    counter++;
    counter %= buffer.getLength();
    setRGB(counter, (int)(counter*13.42), 0, (int)(255-(counter*13.42)));
    update();
  }
}