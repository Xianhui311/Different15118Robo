package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "TeleOp 15118", group = "15118")
public class TeleOp15118 extends LinearOpMode
{

    DcMotor fl, fr, bl, br;

    @Override
    public void runOpMode() throws InterruptedException
    {
        initialize();

        waitForStart();
        Runnable checkP1 = new Runnable() {
            @Override
            public void run() {
                checkP1();
            }
        };
        Runnable checkP2 = new Runnable() {
            @Override
            public void run() {

            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(checkP1, 0, 1, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(checkP2, 0, 1, TimeUnit.MILLISECONDS);
        while(opModeIsActive());
    }

    private void initialize()
    {
        fl = hardwareMap.get(DcMotor.class, "front_left");
        fr = hardwareMap.get(DcMotor.class, "front_right");
        bl = hardwareMap.get(DcMotor.class, "back_left");
        br = hardwareMap.get(DcMotor.class, "back_right");
    }

    private void move(double strafe, double forward, double turn)
    {
        fl.setPower(forward + turn + strafe);
        fr.setPower(forward - turn - strafe);
        bl.setPower(forward + turn - strafe);
        br.setPower((forward - turn + strafe)*-1);
    }

    private void checkP1()
    {
        if(!gamepad1.atRest());
        {
            doTask("move");
        }
    }
    private void checkP2()
    {

    }

    private Thread doTask(String taskName)
    {
        Thread t;
        Runnable r = null;
        if(taskName.equals("move"))
        {
            r = new Runnable() {
                @Override
                public void run() {
                    move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
                }
            };
        }
        t = new Thread(r);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        return t;
    }
}
