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
        System.out.println("Введите желаемую длину пароля (строки) ");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int int_from_reader = Integer.parseInt(reader.readLine());
            generateAndAnalyzePassword(int_from_reader);
            calculateAndPrintAverageTime(int_from_reader);
            plotAverageTimeGraph(int_from_reader);
        } catch (IOException e) {
            System.out.println("Произошла ошибка ввода/вывода: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка преобразования числа: " + e.getMessage());
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

        System.out.println("Среднее время подбора пароля: " + averageTimeToCrackInHours + " часов");
    }

    private static BigInteger calculateCombinations(int passwordLength) {

        final int ALPHABET_SIZE = 62;
        return BigInteger.valueOf(ALPHABET_SIZE).pow(passwordLength);
    }

    public static void plotAverageTimeGraph(int maxPasswordLength) {
        XYSeries series = new XYSeries("Среднее время подбора пароля");

        for (int length = 1; length <= maxPasswordLength; length++) {
            BigInteger combinations = calculateCombinations(length);
            int attackerPerformance = 1000000000;
            BigInteger averageTimeToCrackInSeconds = combinations.divide(BigInteger.valueOf(attackerPerformance));
            series.add(length, averageTimeToCrackInSeconds.doubleValue() / 3600);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Зависимость среднего времени подбора пароля от его длины",
                "Длина пароля", "Среднее время (часы)", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        plot.setRenderer(renderer);

        JFrame frame = new JFrame("График");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

}

/*
Выбор надежного пароля зависит от множества факторов, и его безопасность напрямую влияет на защиту вашей информации.
Вот практические рекомендации по выбору пароля, учитывая алфавит пароля, ценность информации, доступ к которой
защищается, производительность вычислительного средства атакующего и время атаки:

    Длина пароля: Используйте пароли с достаточной длиной. Рекомендуется, чтобы длина пароля была не менее 12 символов.
    Длинные пароли значительно увеличивают количество возможных комбинаций, что затрудняет подбор пароля методом перебора.

    Разнообразие символов: Включите в пароль разнообразные типы символов, такие как буквы верхнего и нижнего регистра,
    цифры и специальные символы. Это усложнит задачу атакующим.

    Избегайте очевидных паролей: Не используйте очень распространенные пароли, такие как "123456" или "password".
    Такие пароли легко взламываются.

    Избегайте словарных слов: Не используйте слова из словаря, даже с небольшими изменениями.
    Атакующие могут использовать словари для подбора паролей.

    Пароли-фразы: Используйте длинные фразы или комбинации слов, которые вам легко запомнить. Например,
    "ЯЛюблюПутешествияВ2019Году!".

    Уникальные пароли: Используйте уникальные пароли для каждого аккаунта. Не используйте один и тот же пароль повторно.

    Двухфакторная аутентификация (2FA): Если это возможно, включите двухфакторную аутентификацию для своих аккаунтов.
    Это добавит дополнительный уровень безопасности.

    Используйте парольный менеджер: Для управления множеством паролей используйте парольный менеджер.
    Это облегчит генерацию, хранение и автоматическое заполнение паролей.

    Периодически меняйте пароли: Регулярно обновляйте свои пароли, особенно для важных аккаунтов.

    Оцените ценность информации: Если у вас есть критически важная информация
    (финансовые данные, медицинская информация), используйте более сложные пароли и усилите безопасность аккаунта.

    Анализируйте производительность атакующего: Попробуйте представить, сколько времени займет атакующему
    взлом вашего пароля. Если у вас есть сомнения в его надежности, укрепите его.

    Следите за новостями: Следите за новостями о сбоях в безопасности и утечках данных.
    Если сервис, на который вы зарегистрированы, был скомпрометирован, смените пароль.

    Обновляйте программное обеспечение: Обновляйте программное обеспечение и операционную систему,
    чтобы закрыть уязвимости, которые могут быть использованы атакующими.

    Обучение безопасности: Обучайтесь основам безопасности и соблюдайте лучшие практики в сфере информационной безопасности.

Запомните, что безопасность паролей играет важную роль в общей безопасности вашей информации. Подходите к
выбору паролей ответственно и используйте современные методы для их создания и управления.
 */