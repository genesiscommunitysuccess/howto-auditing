package tradeaudit.utilities.json_utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Utility class for comparing JSON files.
 */
public class CompareJSON {
    private static final Logger LOG = LoggerFactory.getLogger(CompareJSON.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String REPLY = "REPLY";
    private static final String ROWS_COUNT = "ROWS_COUNT";

    /**
     * Compares two JSON files and logs any mismatches.
     *
     * @param expectedFilePath the path to the expected JSON file
     * @param actualFilePath   the path to the actual JSON file
     * @param primaryKey       the primary key to match rows
     * @param keysToIgnore     comma-separated keys to ignore during comparison
     */
    public static void compareJSONFiles(Path expectedFilePath, Path actualFilePath, String primaryKey, String keysToIgnore) {
        try {
            JsonNode expectedJson = objectMapper.readTree(expectedFilePath.toFile());
            JsonNode actualJson = objectMapper.readTree(actualFilePath.toFile());

            compareRowsCount(expectedJson, actualJson);

            JsonNode expectedRowNode = expectedJson.path(REPLY);
            JsonNode actualRowNode = actualJson.path(REPLY);

            List<Map<String, JsonNode>> mismatchedRows = new ArrayList<>();

            Iterator<JsonNode> expectedRowIterator = expectedRowNode.elements();

            String[] keysToIgnoreArr = getKeysToIgnore(keysToIgnore);

            while (expectedRowIterator.hasNext()) {
                JsonNode expectedRow = expectedRowIterator.next();
                String expectedRowText = expectedRow.path(primaryKey).asText();

                boolean matchFound = false;
                Iterator<JsonNode> actualRowIterator = actualRowNode.elements();
                while (actualRowIterator.hasNext()) {
                    JsonNode actualRow = actualRowIterator.next();
                    String actualRowText = actualRow.path(primaryKey).asText();

                    if (expectedRowText.equals(actualRowText)) {
                        removeKeys(expectedRow, actualRow, keysToIgnoreArr);
                        if (!expectedRow.equals(actualRow)) {
                            Map<String, JsonNode> mismatch = Map.of(
                                    "expected", expectedRow,
                                    "actual", actualRow
                            );
                            mismatchedRows.add(mismatch);
                        }
                        matchFound = true;
                        break;
                    }
                }

                if (!matchFound) {
                    Map<String, JsonNode> mismatch = Map.of(
                            "expected", expectedRow,
                            "actual", objectMapper.createObjectNode()
                    );
                    mismatchedRows.add(mismatch);
                }
            }

            if (!mismatchedRows.isEmpty()) {
                LOG.info("Mismatched rows found:");
                for (Map<String, JsonNode> mismatch : mismatchedRows) {
                    LOG.error("Expected: {}", mismatch.get("expected"));
                    LOG.error("Actual: {}", mismatch.get("actual"));
                }
                throw new JSONComparisonException("Mismatches found in the JSON comparison.", mismatchedRows);
            }
            removeDirectory(actualFilePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compares the row counts in the expected and actual JSON files.
     *
     * @param expectedJson the expected JSON node
     * @param actualJson the actual JSON node
     */
    public static void compareRowsCount(JsonNode expectedJson, JsonNode actualJson) {
        if (!expectedJson.path(ROWS_COUNT).isMissingNode() && !actualJson.path(ROWS_COUNT).isMissingNode()) {
            int expectedRowsCount = expectedJson.path(ROWS_COUNT).asInt();
            int actualRowsCount = actualJson.path(ROWS_COUNT).asInt();
            assertEquals(expectedRowsCount, actualRowsCount, "ROWS_COUNT does not match");
        }
    }

    /**
     * Parses the keys to ignore from a comma-separated string.
     *
     * @param keysToIgnore the comma-separated keys to ignore
     * @return an array of keys to ignore
     */
    public static String[] getKeysToIgnore(String keysToIgnore) {
        if (keysToIgnore == null || keysToIgnore.isEmpty()) {
            return null;
        }
        if (keysToIgnore.contains(",")) {
            return keysToIgnore.split(",");
        } else {
            return new String[]{keysToIgnore};
        }
    }

    /**
     * Removes the specified keys from the given JSON nodes.
     *
     * @param expectedRow the expected JSON node
     * @param actualRow the actual JSON node
     * @param keysToIgnoreArr the array of keys to ignore
     */
    public static void removeKeys(JsonNode expectedRow, JsonNode actualRow, String[] keysToIgnoreArr) {
        if (keysToIgnoreArr != null) {
            if (expectedRow instanceof ObjectNode && actualRow instanceof ObjectNode) {
                for (String key : keysToIgnoreArr) {
                    ((ObjectNode) expectedRow).remove(key);
                    ((ObjectNode) actualRow).remove(key);
                }
            }
        }
    }

    /**
     * Removes the directory containing the specified file.
     *
     * @param directory the path to the file
     */
    public static void removeDirectory(Path directory) {
        try {
            if (Files.isDirectory(directory)) {
                FileUtils.deleteDirectory(directory.toFile());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}