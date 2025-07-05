package rs.ac.uns.ftn.onlybunsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.AnalyticsDto;
import rs.ac.uns.ftn.onlybunsapp.service.AnalyticsService;

import java.sql.Timestamp;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/analytics", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnalyticsController {

    @Autowired
    private  AnalyticsService analyticsService;

    @PostMapping("/activity")
    public ResponseEntity<Map<String, Integer>> getActivity(@RequestBody AnalyticsDto dateRangeDTO) {
        Timestamp startDate = dateRangeDTO.getStartDate();
        Timestamp endDate = dateRangeDTO.getEndDate();

        System.out.println("StartDate: " + startDate);
        System.out.println("EndDate: " + endDate);

        return ResponseEntity.ok(analyticsService.getActivityCounts(startDate, endDate));
    }
    @GetMapping("/user-activity-stats")
    public ResponseEntity<Map<String, Double>> getUserActivityStats() {
        Map<String, Double> stats = analyticsService.getUserActivityStats();
        return ResponseEntity.ok(stats);
    }
}
