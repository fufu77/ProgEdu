package fcu.selab.progedu.db;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewSettingMetricsDbManager {

    private static ReviewSettingMetricsDbManager dbManager = new ReviewSettingMetricsDbManager();

    public static ReviewSettingMetricsDbManager getInstance() { return dbManager; }

    private IDatabase database = new MySqlDatabase();

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewSettingMetricsDbManager.class);

    /**
     * Insert review setting metrics into db
     *
     * @param rsId              review setting Id
     * @param rmId              review metrics Id
     */
    public void insertReviewSettingMetrics(int rsId, int rmId) {
        String query = "INSERT INTO Review_Setting_Metrics(rsId, rmId) VALUES(?,?)";

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, rsId);
            preStmt.setInt(2, rmId);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Get review metrics from review setting metrics by specific assignment id
     *
     * @param rsId              review setting Id
     */
    public List<Integer> getReviewSettingMetricsByAssignmentId (int rsId) {
        String query = "SELECT rmId FROM Review_Setting_Metrics WHERE rsId = ?";
        List<Integer> metricsList = new ArrayList<>();

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, rsId);
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    int rmId = rs.getInt("rmId");
                    metricsList.add(rmId);
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
        return metricsList;
    }

    /**
     * Delete review metrics from review setting metrics by specific assignment id
     *
     * @param rsId              review setting Id
     */
    public void deleteReviewSettingMetricsByAssignmentId(int rsId) {
        String query = "DELETE FROM Review_Setting_Metrics WHERE rsId = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, rsId);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

}
