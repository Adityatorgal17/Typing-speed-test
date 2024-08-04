import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class Frame extends JFrame {
    private long startTime; // To track the start time for WPM calculation
    private JLabel timeLabel; // Label to display the remaining time

    // Constructor to set up the frame
    public Frame() {
        this.setSize(1000, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Typing Speed Test");
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        // Set the frame icon
        ImageIcon image = new ImageIcon("cartoon-funny-donkey.jpg");
        this.setIconImage(image.getImage());

        // Initialize the panel
        setpanel();

        this.setVisible(true);
    }

    // Method to set up the initial panel
    public void setpanel() {
        // Create and set up the heading panel
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.gray);

        JLabel heading = new JLabel();
        heading.setFont(new Font("Serif", Font.BOLD, 40));
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setText("Press The Start Button To Begin");
        panel1.add(heading);

        panel1.setBounds(0, 0, 1000, 60);
        this.add(panel1);

        // Create and set up the button panel
        JPanel panel2 = new JPanel();
        panel2.setBounds(0, 60, 1000, 440);
        panel2.setLayout(null);

        JButton button = new JButton();
        button.setBounds(420, 150, 150, 75);
        button.setText("Start");
        button.setBackground(Color.GREEN);
        button.setFont(new Font("Serif", Font.BOLD, 40));
        button.setFocusPainted(false);
        panel2.add(button);
        this.add(panel2);

        // Add action listener to the button to start the typing test
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                source();
            }
        });
    }

    // Method to set up the source panel and start the typing test
    private void source() {
        // Create and set up the main panel
        JPanel panel3 = new JPanel();
        panel3.setBackground(Color.gray);
        panel3.setBounds(0, 0, 1000, 500);
        panel3.setLayout(null);

        // Display the shuffled text in a non-editable text area
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.ITALIC, 28));
        textArea.setForeground(Color.BLUE);
        String ShuffledText = Text.text(); // Assuming Text.text() provides shuffled text
        textArea.setText(ShuffledText);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(10, 10, 965, 160);
        textArea.setEditable(false);
        panel3.add(textArea);

        getContentPane().removeAll();
        this.add(panel3);

        // Display instructions in another text area
        JTextArea textarea3 = new JTextArea();
        textarea3.setFont(new Font("Serif", Font.BOLD, 30));
        textarea3.setBounds(10, 180, 965, 60);
        textarea3.setForeground(Color.red);
        textarea3.setText("Copy the above text below");
        panel3.add(textarea3);

        // Create and set up the user input text area (initially non-editable)
        JTextArea textArea2 = new JTextArea();
        textArea2.setFont(new Font("Serif", Font.ITALIC, 28));
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);
        textArea2.setBounds(10, 250, 965, 200);
        textArea2.setEditable(false);
        panel3.add(textArea2);

        // Add a label to display the remaining time
        timeLabel = new JLabel("Remaining time: 30 seconds", JLabel.CENTER);
        timeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        timeLabel.setBounds(10, 460, 965, 30);
        panel3.add(timeLabel);

        // Timer to handle the initial countdown before typing can begin
        Timer countdownTimer = new Timer(1000, new ActionListener() {
            int countdown = 5;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdown > 0) {
                    textArea2.setText("Start typing in " + countdown);
                    countdown--;
                } else {
                    ((Timer) e.getSource()).stop();
                    textArea2.setText("");
                    textArea2.setEditable(true);
                    textArea2.requestFocus();
                    startTime = System.currentTimeMillis(); // Start time when user can type

                    // Timer to handle the typing period
                    Timer inputTimer = new Timer(1000, new ActionListener() {
                        int inputTimeLeft = 30;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (inputTimeLeft >= 0) {
                                timeLabel.setText("Remaining time: " + inputTimeLeft + " seconds");
                                textarea3.setText("Time remaining: " + inputTimeLeft + " seconds"); // Update textarea3 with the countdown
                                inputTimeLeft--;
                                timeLabel.revalidate();
                                timeLabel.repaint();
                            } else {
                                ((Timer) e.getSource()).stop();
                                textArea2.setEditable(false);
                                timeLabel.setText("Time's up!");
                                textarea3.setText("Time's up!");
                                String userInput = textArea2.getText();
                                Calculate(userInput, ShuffledText);
                            }
                        }
                    });
                    inputTimer.setInitialDelay(0);
                    inputTimer.start();
                }
            }
        });
        countdownTimer.setInitialDelay(0);
        countdownTimer.start();

        this.revalidate();
        this.repaint();
    }

    // Method to calculate and display typing speed and accuracy
    private void Calculate(String userInput, String originalText) {
        // Calculate the elapsed time in minutes
        long elapsedTime = System.currentTimeMillis() - startTime; // in milliseconds
        double elapsedMinutes = elapsedTime / 60000.0; // convert to minutes

        if (elapsedMinutes == 0) {
            elapsedMinutes = 1; // Prevent division by zero
        }

        // Calculate word count and WPM
        int wordCount = countWords(userInput);
        int wpm = (int) (wordCount / elapsedMinutes);

        // Calculate correct words and accuracy
        int correctWords = countCorrectWords(userInput, originalText);
        double accuracy = (correctWords / (double) wordCount) * 100;

        // Display the results
        JPanel panel4 = new JPanel();
        panel4.setBackground(Color.white);
        panel4.setBounds(0, 0, 1000, 500);
        panel4.setLayout(new GridBagLayout());

        JLabel result = new JLabel("<html>WPM: " + wpm + "<br>Accuracy: " + String.format("%.2f", accuracy) + "%</html>");
        result.setFont(new Font("Arial", Font.BOLD, 40));
        result.setForeground(Color.RED);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel4.add(result, gbc);

        getContentPane().removeAll();
        this.add(panel4);

        this.revalidate();
        this.repaint();
    }

    // Method to count the number of words in a given text
    private int countWords(String text) {
        return text.trim().isEmpty() ? 0 : text.split("\\s+").length;
    }

    // Method to count the number of correct words in the user's input
    private int countCorrectWords(String userInput, String originalText) {
        List<String> userWords = Arrays.asList(userInput.trim().split("\\s+"));
        List<String> originalWords = Arrays.asList(originalText.trim().split("\\s+"));
        int correctWords = 0;

        for (int i = 0; i < Math.min(userWords.size(), originalWords.size()); i++) {
            if (userWords.get(i).equals(originalWords.get(i))) {
                correctWords++;
            }
        }
        return correctWords;
    }

}

