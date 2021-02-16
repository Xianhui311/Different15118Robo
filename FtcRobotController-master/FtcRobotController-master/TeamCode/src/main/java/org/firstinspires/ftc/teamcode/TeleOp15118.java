//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@TeleOp(name = "TeleOp 15118", group = "15118")
//public class TeleOp15118 extends LinearOpMode
//{
//
//    DcMotor fl, fr, bl, br;
//
//    @Override
//    public void runOpMode() throws InterruptedException
//    {
//        initialize();
//
//        waitForStart();
//        final Runnable checkP1 = new Runnable() {
//            @Override
//            public void run() {
//                checkP1();
//            }
//        };
//        Runnable checkP2 = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
//        executor.scheduleAtFixedRate(checkP1, 0, 1, TimeUnit.MILLISECONDS);
//        executor.scheduleAtFixedRate(checkP2, 0, 1, TimeUnit.MILLISECONDS);
//    }
//
//    private void initialize()
//    {
//        fl = hardwareMap.get(DcMotor.class, "front_left");
//        fr = hardwareMap.get(DcMotor.class, "front_right");
//        bl = hardwareMap.get(DcMotor.class, "back_left");
//        br = hardwareMap.get(DcMotor.class, "back_right");
//    }
//
//    private void move(double strafe, double forward, double turn)
//    {
//        fl.setPower(forward + turn + strafe);
//        fr.setPower(forward - turn - strafe);
//        bl.setPower(forward + turn - strafe);
//        br.setPower(forward - turn + strafe);
//    }
//
//    private void checkP1()
//    {
//        if(!gamepad1.atRest());
//        {
//            doTask("move");
//        }
//    }
//    private void checkP2()
//    {
//
//    }
//
//    private Thread doTask(String taskName)
//    {
//        Thread t;
//        Runnable r = null;
//        if(taskName.equals("move"))
//        {
//            r = new Runnable() {
//                @Override
//                public void run() {
//                    move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
//                }
//            };
//        }
//        t = new Thread(r);
//        t.setPriority(Thread.MAX_PRIORITY);
//        t.start();
//        return t;
//    }
//}
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp", group = "Linear Opmode")
public class TeleOp15118 extends LinearOpMode
{

    DcMotor fl, fr, bl, br, intake, outtake;
    Servo intakeSweeper, intakeRaiser;

    boolean raised = false;
    boolean clicked = false;

    @Override
    public void runOpMode() throws InterruptedException
    {
        initialize();

        waitForStart();
        while(opModeIsActive())
        {
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = gamepad1.right_stick_x;
            final double v1;
            final double v2;
            final double v3;
            final double v4;

            v1 = r * Math.cos(robotAngle) - rightX;
            v2 = r * Math.sin(robotAngle) + rightX;
            v3 = r * Math.sin(robotAngle) - rightX;
            v4 = r * Math.cos(robotAngle) + rightX;

            if(gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0) {
                //Inverted Driving, Normal Turning and Strafing
                fl.setPower(v1 * 0.75);
                fr.setPower(v2 * 0.75);
                bl.setPower(v3 * 0.75);
                br.setPower(v4 * 0.75);
            }else{
                //Normal Driving, Inverted Turning and Strafing
                fl.setPower(v1 * -0.75);
                fr.setPower(v2 * -0.75);
                bl.setPower(v3 * -0.75);
                br.setPower(v4 * -0.75);
            }


            if(gamepad1.right_bumper || gamepad1.left_bumper)
            {
                outtake();
            }
            else if(!gamepad1.right_bumper)
                outtake.setPower(0);
            if(gamepad1.right_trigger != 0 || gamepad1.left_trigger != 0)
            {
                intake();
            }
            if(gamepad1.a)
            {
                intakeSweeper.setPosition(-1);
                sleep(250);
                intakeSweeper.setPosition(0.5);
            }
            if(gamepad1.x && !raised && clicked == false)
            {
                intakeRaiser.setPosition(1);
                clicked = true;
                raised = true;
            }
            if(gamepad1.x && raised && clicked == false)
            {
                    intakeRaiser.setPosition(-1);
                    raised = false;
                    clicked = true;
            }
            if (!gamepad1.x)
            {
                clicked = false;
            }
        }


    }

    public void initialize()
    {
        fl = hardwareMap.get(DcMotor.class, "front_left");
        fr = hardwareMap.get(DcMotor.class, "front_right");
        bl = hardwareMap.get(DcMotor.class, "back_left");
        br = hardwareMap.get(DcMotor.class, "back_right");

        intake = hardwareMap.get(DcMotor.class, "intake");
        outtake = hardwareMap.get(DcMotor.class, "outtake");

        intakeRaiser = hardwareMap.get(Servo.class, "intake_raiser");

        intakeSweeper = hardwareMap.get(Servo.class, "intake_sweeper");
        br.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void move(double strafe, double forward, double turn)
    {
        while(gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0 || gamepad1.right_stick_x != 0)
        {
            fl.setPower((forward + turn + strafe));
            fr.setPower((forward - turn - strafe));
            bl.setPower((forward + turn - strafe));
            br.setPower((forward - turn + strafe) * -1);
        }
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    public void intake()
    {
        //CHANGE THE POWER OF THE INTAKE HERE
        if(gamepad1.right_trigger > 0)
        {
            intake.setPower(gamepad1.right_trigger);
        }
        if(gamepad1.left_trigger > 0)
        {
            intake.setPower(gamepad1.left_trigger * -1);
        }
        else
            intake.setPower(0);
    }

    public void outtake()
    {
            outtake.setPower(-1);
    }

}
