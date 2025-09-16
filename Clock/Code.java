
public class ClockDisplay {
    private NumberDisplay hours;
    private NumberDisplay minutes;
    private NumberDisplay seconds;
    private String displayString; // simulates the actual display

    /**
     * Constructor for ClockDisplay objects. This constructor
     * creates a new clock set at 00:00:00.
     */
    public ClockDisplay() {
        hours = new NumberDisplay(24);
        minutes = new NumberDisplay(60);
        seconds = new NumberDisplay(60);
        updateDisplay();
    }

    /**
     * Constructor for ClockDisplay objects. This constructor
     * creates a new clock set at the time specified by the
     * parameters.
     */
    public ClockDisplay(int hour, int minute, int second) {
        hours = new NumberDisplay(24);
        minutes = new NumberDisplay(60);
        seconds = new NumberDisplay(60);
        setTime(hour, minute, second);
    }

    /**
     * This method should get called once every second - it
     * makes the clock display go one second forward.
     */
    public void timeTick() {
        seconds.increment();
        if (seconds.getValue() == 0) { // seconds rolled over
            minutes.increment();
            if (minutes.getValue() == 0) { // minutes rolled over
                hours.increment();
            }
        }
        updateDisplay();
    }

    /**
     * Set the time of the display to the specified hour,
     * minute, and second.
     */
    public void setTime(int hour, int minute, int second) {
        hours.setValue(hour);
        minutes.setValue(minute);
        seconds.setValue(second);
        updateDisplay();
    }

    /**
     * Return the current time of this display in the format
     * HH:MM:SS.
     */
    public String getTime() {
        return displayString;
    }

    /**
     * Update the internal string that represents the display.
     */
    private void updateDisplay() {
        displayString = hours.getDisplayValue() + ":" +
                        minutes.getDisplayValue() + ":" +
                        seconds.getDisplayValue();
    }
}

/**
* The NumberDisplay class represents a digital number
* display that can hold values from zero to a given limit.
* The limit can be specified when creating the display. The
* values range from zero (inclusive) to limit-1. If used, for
* example, for the seconds on a digital clock, the limit
* would be 60, resulting in display values from 0 to 59.
* When incremented, the display automatically rolls over to
* zero when reaching the limit.
*
* @author Michael Kölling and David J. Barnes
* @version 2011.07.31
*/
public class NumberDisplay
{
private int limit;
private int value;
/**
* Constructor for objects of class NumberDisplay
*/
public NumberDisplay(int rollOverLimit)
{
limit = rollOverLimit;
value = 0;
}
/**
* Return the current value.
*/


public int getValue()
{
return value;
}
/**
* Set the value of the display to the new specified
* value. If the new value is less than zero or over the
* limit, do nothing.
*/
public void setValue(int replacementValue)
{
if((replacementValue >= 0) &&
(replacementValue < limit)) {
value = replacementValue;
}
}
/**
* Return the display value (that is, the current value
* as a two-digit String. If the value is less than ten,
* it will be padded with a leading zero).
*/
public String getDisplayValue()
{
if(value < 10) {
return "0" + value;
}
else {
return "" + value;
}
}
/**
* Increment the display value by one, rolling over to zero if
* the limit is reached.
*/
public void increment()
{
value = (value + 1) % limit;
}
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ClockGUI.java
 * GUI frontend using Swing. Contains main().
 */
public class ClockGUI extends JFrame {
    private ClockDisplay clock;
    private JLabel timeLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton setButton;
    private JTextField hourField;
    private JTextField minuteField;
    private JTextField secondField;
    private javax.swing.Timer timer;

    public ClockGUI() {
        super("Clock GUI - HH:MM:SS");
        clock = new ClockDisplay(0, 0, 0);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateTimeLabel();
    }

    private void initComponents() {
        timeLabel = new JLabel(clock.getTime(), SwingConstants.CENTER);
        timeLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        setButton = new JButton("Set Time");

        hourField = new JTextField(2);
        minuteField = new JTextField(2);
        secondField = new JTextField(2);

        hourField.setText("00");
        minuteField.setText("00");
        secondField.setText("00");

        JPanel top = new JPanel(new BorderLayout());
        top.add(timeLabel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 6));
        controls.add(new JLabel("Hour:"));
        controls.add(hourField);
        controls.add(new JLabel("Min:"));
        controls.add(minuteField);
        controls.add(new JLabel("Sec:"));
        controls.add(secondField);
        controls.add(setButton);
        controls.add(startButton);
        controls.add(stopButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, BorderLayout.CENTER);
        getContentPane().add(controls, BorderLayout.SOUTH);

        timer = new javax.swing.Timer(1000, e -> {
            clock.timeTick();
            updateTimeLabel();
        });

        startButton.addActionListener(e -> {
            if (!timer.isRunning()) timer.start();
        });

        stopButton.addActionListener(e -> {
            if (timer.isRunning()) timer.stop();
        });

        setButton.addActionListener(e -> {
            try {
                int h = Integer.parseInt(hourField.getText().trim());
                int m = Integer.parseInt(minuteField.getText().trim());
                int s = Integer.parseInt(secondField.getText().trim());
                clock.setTime(h, m, s);
                updateTimeLabel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Masukkan angka valid untuk jam/menit/detik.",
                    "Input error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                    "Nilai tidak valid: " + ex.getMessage(),
                    "Input error", JOptionPane.ERROR_MESSAGE);
            }
        });

        ActionListener setOnEnter = e -> setButton.doClick();
        hourField.addActionListener(setOnEnter);
        minuteField.addActionListener(setOnEnter);
        secondField.addActionListener(setOnEnter);
    }

    private void updateTimeLabel() {
        timeLabel.setText(clock.getTime());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClockGUI gui = new ClockGUI();
            gui.setVisible(true);
        });
    }
}

