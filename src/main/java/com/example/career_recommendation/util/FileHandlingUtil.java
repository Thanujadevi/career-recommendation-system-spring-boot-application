package com.example.career_recommendation.util;

import com.example.career_recommendation.model.RecommendationResult;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * FileHandlingUtil — demonstrates Java File I/O concepts.
 *
 * FIX: Original code used new FileWriter() without try-with-resources,
 * which caused a resource leak if an exception occurred.
 * Now uses try-with-resources (AutoCloseable) to guarantee the writer
 * is always closed.
 *
 * Changed export() to return the text content as a String
 * (used in the download endpoint) rather than writing to the server filesystem.
 */
@Component
public class FileHandlingUtil {

    /**
     * Build a plain-text report of recommendation results.
     * Returns the content as a String for download via HTTP response.
     *
     * @param results   Ranked recommendation results for the student
     * @param studentId The student's ID (used in the report header)
     * @return Multi-line text content suitable for a .txt download
     * @throws IOException if writing fails
     */
    public String export(List<RecommendationResult> results, Long studentId) throws IOException {

        // try-with-resources ensures writer.close() is always called
        try (StringWriter sw = new StringWriter();
             BufferedWriter writer = new BufferedWriter(sw)) {

            writer.write("============================================");
            writer.newLine();
            writer.write("  CAREER RECOMMENDATION REPORT");
            writer.newLine();
            writer.write("  Student ID : " + studentId);
            writer.newLine();
            writer.write("  Student    : " +
                    (results.isEmpty() ? "N/A" : results.get(0).getStudent().getFullName()));
            writer.newLine();
            writer.write("============================================");
            writer.newLine();
            writer.newLine();

            for (RecommendationResult r : results) {
                writer.write(String.format(
                        "Rank %-3d | %-30s | Score: %6.2f | Domain: %s",
                        r.getRank(),
                        r.getCareer().getCareerName(),
                        r.getTotalScore(),
                        r.getCareer().getDomain()
                ));
                writer.newLine();
            }

            writer.flush();
            return sw.toString();
        }
    }
}
