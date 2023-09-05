package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.math.BigInteger;
import java.util.Random;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Enter desired length (string)");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int int_from_reader = Integer.parseInt(reader.readLine());
            generateAndAnalyzePassword(int_from_reader);
            calculateAndPrintAverageTime(int_from_reader);
            plotAverageTimeGraph(int_from_reader);
        } catch (IOException e) {
            System.out.println("An I/O error occurred: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Number conversion error: " + e.getMessage());
        }
    }

    public static String generateRandomPassword(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }
        return password.toString();
    }

    public static void generateAndAnalyzePassword(int passwordLength) {
        String generatedPassword = generateRandomPassword(passwordLength);

        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (char c : generatedPassword.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            char symbol = entry.getKey();
            int frequency = entry.getValue();
            System.out.println(symbol + ": " + frequency);
        }
    }

    public static void calculateAndPrintAverageTime(int passwordLength) {
        BigInteger combinations = calculateCombinations(passwordLength);

        int attackerPerformance = 1000000000;

        BigInteger averageTimeToCrackInSeconds = combinations.divide(BigInteger.valueOf(attackerPerformance));

        BigInteger averageTimeToCrackInHours = averageTimeToCrackInSeconds.divide(BigInteger.valueOf(3600));

        System.out.println("Average password guess time: " + averageTimeToCrackInHours + " hours");
    }

    private static BigInteger calculateCombinations(int passwordLength) {

        final int ALPHABET_SIZE = 62;
        return BigInteger.valueOf(ALPHABET_SIZE).pow(passwordLength);
    }

    public static void plotAverageTimeGraph(int maxPasswordLength) {
        XYSeries series = new XYSeries("Average password guess time: ");

        for (int length = 1; length <= maxPasswordLength; length++) {
            BigInteger combinations = calculateCombinations(length);
            int attackerPerformance = 1000000000;
            BigInteger averageTimeToCrackInSeconds = combinations.divide(BigInteger.valueOf(attackerPerformance));
            series.add(length, averageTimeToCrackInSeconds.doubleValue() / 3600);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Dependence of the average password guessing time on its length",
                "Password length", "Mean Time (Hours)", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        plot.setRenderer(renderer);

        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

}
