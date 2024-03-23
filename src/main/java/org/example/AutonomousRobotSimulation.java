package org.example;

import java.util.Random;

public class AutonomousRobotSimulation {
    private static final int GRID_SIZE = 10;
    private static final int NUM_ELEVATORS = 1;

    static class Robot {
        int x, y;

        Robot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void move(String direction) {
            switch (direction) {
                case "up":
                    if (x > 0) x--;
                    break;
                case "down":
                    if (x < GRID_SIZE - 1) x++;
                    break;
                case "left":
                    if (y > 0) y--;
                    break;
                case "right":
                    if (y < GRID_SIZE - 1) y++;
                    break;
            }
        }
    }

    static class Environment {
        Robot robot;
        int[] destination = new int[]{GRID_SIZE - 1, GRID_SIZE - 1};
        int[][] elevators = new int[NUM_ELEVATORS][2];
        int[][] chimes = new int[1][2]; // Only one chime in this case
        Random random = new Random();

        Environment() {
            robot = new Robot(0, 0);
            // Set device positions
            elevators[0] = new int[]{5, 5}; // Elevator position
            chimes[0] = new int[]{8, 2};    // Chime bell position
        }

        boolean checkGoal() {
            return robot.x == destination[0] && robot.y == destination[1];
        }

        int[] ringBell() {
            return new int[]{2, 3}; // Call bell position
        }

        int[] moveElevator() {
            return elevators[random.nextInt(NUM_ELEVATORS)];
        }

        int[] ringChime() {
            return chimes[0]; // Only one chime
        }
    }

    public static void main(String[] args) {
        Environment env = new Environment();
        int[][] waypoints = {{2, 3}, {5, 5}, {8, 2}}; // Waypoints

        for (int[] waypoint : waypoints) {
            while (env.robot.x != waypoint[0] || env.robot.y != waypoint[1]) {
                String direction = getNextDirection(env.robot.x, env.robot.y, waypoint[0], waypoint[1]);
                env.robot.move(direction);
                System.out.println("Robot is at position: (" + env.robot.x + ", " + env.robot.y + ")");
            }
            checkConstrains(env);
        }

        while (!env.checkGoal()) {
            String direction = getNextDirection(env.robot.x, env.robot.y, env.destination[0], env.destination[1]);
            env.robot.move(direction);

            checkConstrains(env);

            System.out.println("Robot is at position: (" + env.robot.x + ", " + env.robot.y + ")");
        }
        System.out.println("Destination reached!");
    }

    private static void checkConstrains(Environment env) {
        if (contains(env.elevators, env.robot.x, env.robot.y)) {
            System.out.println("Robot is entering elevator.");
            int[] elevatorPos = env.moveElevator();
            env.robot.x = elevatorPos[0];
            env.robot.y = elevatorPos[1];
            System.out.println("Robot has exited elevator.");
        }

        if (contains(env.chimes, env.robot.x, env.robot.y)) {
            System.out.println("Robot hears a chime.");
            int[] chimePos = env.ringChime();
            env.robot.x = chimePos[0];
            env.robot.y = chimePos[1];
            System.out.println("Robot has oriented itself.");
        }
    }

    static boolean contains(int[][] array, int x, int y) {
        for (int[] arr : array) {
            if (arr[0] == x && arr[1] == y) return true;
        }
        return false;
    }

    static String getNextDirection(int startX, int startY, int endX, int endY) {
        if (startX < endX) return "down";
        if (startX > endX) return "up";
        if (startY < endY) return "right";
        if (startY > endY) return "left";
        return ""; // Should never reach here
    }
}