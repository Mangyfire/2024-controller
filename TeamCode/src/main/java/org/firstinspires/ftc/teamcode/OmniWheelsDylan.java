/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.List;
import java.util.ArrayList;

/*
 * This file contains an example of a Linear "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode is executed.
 *
 * This particular OpMode illustrates driving a 4-motor Omni-Directional (or Holonomic) robot.
 * This code will work with either a Mecanum-Drive or an X-Drive train.
 * Both of these drives are illustrated at https://gm0.org/en/latest/docs/robot-design/drivetrains/holonomic.html
 * Note that a Mecanum drive must display an X roller-pattern when viewed from above.
 *
 * Also note that it is critical to set the correct rotation direction for each motor.  See details below.
 *
 * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
 * Each motion axis is controlled by one Joystick axis.
 *
 * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
 * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
 * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
 *
 * This code is written assuming that the right-side motors need to be reversed for the robot to drive forward.
 * When you first test your robot, if it moves backward when you push the left stick forward, then you must flip
 * the direction of all 4 motors (see code below).
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="OmniWheelsDylan 0.1", group="OmniOp")
@Disabled
public class OmniWheelsDylan extends LinearOpMode {

    // Declare OpMode members for each of the 4 wheel motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    // Declare OpMode members for each of the 4 arm motors.
    private DcMotor Armpit = null;
    private DcMotor Elbow = null;
    private DcMotor ArmRotation = null;
    private Servo handgrip = null;

    list<DCMotor> allMotors = new ArrayList<>();
    list<Servo> allServos = new ArrayList<>();
    
    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        Armpit = hardwareMap.get(DcMotor.class, "Armpit");
        Elbow = hardwareMap.get(DcMotor.class, "Elbow");
        ArmRotation = hardwareMap.get(DcMotor.class, "ArmRotation");
        handgrip = hardwarewareMap.get(Servo.class, "third-servo");

        allMotors.add(leftFrontDrive);
        allMotors.add(rightFrontDrive);
        allMotors.add(rightBackDrive);
        allMotors.add(leftBackDrive);
        allMotors.add(Armpit);
        allMotors.add(Elbow);
        allMotors.add(ArmRotation);

        allServos.add(handgrip);

        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.

        // Wheel motor directions
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        Armpit.setDirection(DcMotor.Direction.FORWARD);
        Elbow.setDirection(DcMotor.Direction.FORWARD);

        // handgrip config
        final double INCREMENT = 0.01;
        final int CYCLEMS = 50;
        final double MAX_POS = 1.0;
        final double MIN_POS = 0.0;

        // Wait for the game to start (driver presses START)
        telemetry.addData("High Five", "We Roboted!!!");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;
            
            // MOVEMENT CONTROLLS 

            // wheel sensitivity controls
            double  wsens  = 1;
            double  tLinearSpeed = gamepad1.right_trigger;
            double  tTurnSpeed = gamepad1.left_trigger;

            if (tLinearSpeed == 0){
                double tLinearSpeed = 1;
            }
            if (tTurnSpeed == 0){
                double tTurnSpeed = 1;
            }
            // wheel movement controls
            
            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value -- forward/backward
            double lateral =  gamepad1.left_stick_x;  // Strafe left/right
            double yaw     =  gamepad1.right_stick_x; //rotate left/right        

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            
            double leftFrontPower  = (axial * wsens * tLinearSpeed) + (lateral * wsens * tlinearSpeed) + (yaw * wsens * tTurnSpeed);
            double rightFrontPower = (axial * wsens * tLinearSpeed) - (lateral * wsens * tlinearSpeed) - (yaw * wsens * tTurnSpeed);
            double leftBackPower   = (axial * wsens * tLinearSpeed) - (lateral * wsens * tlinearSpeed) + (yaw * wsens * tTurnSpeed);
            double rightBackPower  = (axial * wsens * tLinearSpeed) + (lateral * wsens * tlinearSpeed) - (yaw * wsens * tTurnSpeed);

            // Grip control

            boolean grip   = right_bumper;
            boolean ungrip = left_bumper;

            if (grip == true) {
                handgrip.setPosition(Math.min(1.0,handgrip.getPosition()+0.01));
            }
            
            if (ungrip == true) {
                handgrip.setPosition(Math.min(0.0,handgrip.getPosition()-0.01));
            }

            // Arm sensitivity
            double  asens  = 1;
            double  t2LinearSpeed = gamepad2.right_trigger;
            double  t2TurnSpeed = gamepad2.left_trigger;

            //Arm control
            double ArmSegment1   = gamepad2.right_stick_y; 
            double ArmRotate =  gamepad2.right_stick_x;  
            double ArmSegment2 =  gamepad2.left_stick_y;  
            

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.

            /*
            leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
            leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
            rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
            rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad
            */

            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            //Send calculated power to Arm
            Armpit.setPower(ArmSegment1);
            Elbow.setPower(ArmSegment2);
            ArmRotation.setPower(ArmRotate);

            // Show the elapsed game time and wheel power.
            // telemetry.addData("Status", "Run Time: " + runtime.toString());
            // telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            // telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);

            for (DcMotor disMotor : allMotors) {
                telemetry.addData("MotorSpeed", thisMotor.getSpeed());
            }
            for (Servo thisServo : allServos) {
                telemetry.addData("ServoPosition", thisServo.getPosition());
            }
            telemetry.addData ("Axial", axial);
            telemetry.addData ("Lateral", lateral);
            telemetry.addData ("Yaw", yaw);

            
            telemetry.update();
        }
    }}
 

/*
index (may be inacurate (if so command f))

72     Declare OpMode members for each of the 4 wheel motors. 
79     Declare OpMode members for each of the 4 arm motors.
93     gets hardware maps for motors
112    Wheel motor directions
120    handgrip config
126    Wait for the game to start (driver presses START) + telemetry
137    Movement controls start
139    wheel sensitivity controls
150    wheel movement controls
165    Grip control

*/

//Editors
//-.. -.-- ..-. .- -.
