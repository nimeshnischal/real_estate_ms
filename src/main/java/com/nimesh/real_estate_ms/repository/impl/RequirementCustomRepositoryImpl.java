package com.nimesh.real_estate_ms.repository.impl;

import com.nimesh.real_estate_ms.entity.Property;
import com.nimesh.real_estate_ms.entity.Requirement;
import com.nimesh.real_estate_ms.repository.RequirementCustomRepository;
import com.nimesh.real_estate_ms.repository.util.CustomPage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.nimesh.real_estate_ms.config.AppConstants.*;

public class RequirementCustomRepositoryImpl implements RequirementCustomRepository {

    private static final double DISTANCE_STEPS_COUNT = MAX_DISTANCE_IN_MILES/DISTANCE_STEP_IN_MILES;
    private static final double DISTANCE_PERCENT_STEP = DISTANCE_WEIGHTAGE_PERCENT/DISTANCE_STEPS_COUNT;
    private static final double BUDGET_MULTIPLIER_CONSTANT = (1/MAX_PRICE_RANGE_RATIO) * (BUDGET_DIFF_TOTAL_STEPS - 1);
    private static final double BUDGET_DIFF_PERCENT_STEP = BUDGET_WEIGHTAGE_PERCENT/ BUDGET_DIFF_TOTAL_STEPS;
    private static final int BEDROOM_DIFF_PERCENT_TOTAL_STEPS = MAX_RANGE_OF_BEDROOM + 1;
    private static final int BATHROOM_DIFF_PERCENT_TOTAL_STEPS = MAX_RANGE_OF_BATHROOM + 1;

    @PersistenceContext
    private EntityManager em;

    @Override
    public CustomPage<Requirement> findAllByProperty(Property property, long offset, int pageSize) {
        String queryString =
            "SELECT *, " +
                getTotalPercentQuery() + " as match_percent " +
            getInnerFromQuery(property, false) +
            "HAVING match_percent >= "+ MINIMUM_SUCCESSFUL_MATCH_PERCENT + " " +
            "ORDER BY match_percent DESC " +
            "LIMIT "+ offset +", "+ pageSize;
        List<Requirement> requirements = em.createNativeQuery(queryString, Requirement.class).getResultList();
        long count = getCountByProperty(property);
        return new CustomPage<>(requirements, offset, pageSize, count);
    }

    private long getCountByProperty(Property property) {
        String countQuery = "SELECT count(*) " + getInnerFromQuery(property, true);
        return ((Number) em.createNativeQuery(countQuery).getSingleResult()).longValue();
    }

    private static String getInnerFromQuery(Property property, boolean isCountQuery) {
        Double propertyLatitude = property.getLatitude();
        Double propertyLongitude = property.getLongitude();
        Double propertyPrice = property.getPrice();
        Double propertyBedroomCount = property.getBedroomCount();
        Integer propertyBathroomCount = property.getBathroomCount();
        return "FROM (" +
                    "SELECT " +
                        "*, " +
                        "(" +
                            MILES_DISTANCE_CONSTANT +
                            " * acos (" +
                                " cos ( radians("+ propertyLatitude +") )" +
                                " * cos( radians( latitude ) )" +
                                " * cos( radians( longitude ) - radians("+ propertyLongitude +") )" +
                                " + sin ( radians("+ propertyLatitude +") )" +
                                " * sin( radians( latitude ) )" +
                            ")" +
                        ") AS distance, " +
                        "(" +
                            "CEILING( " +
                                "(" +
                                    BUDGET_MULTIPLIER_CONSTANT + " * " +
                                    "(" +
                                        "ABS( mean_budget - " + propertyPrice + ") - range_budget " +
                                    ")" +
                                ") / mean_budget " +
                            ")" +
                        ") as price_diff_ratio, " +
                        "(" +
                            "ABS(((max_bedrooms + min_bedrooms)/2)  - "+ propertyBedroomCount +") - ((max_bedrooms - min_bedrooms)/2)" +
                        ") as bedroom_count_diff, " +
                        "(" +
                            "ABS(((max_bathrooms + min_bathrooms)/2)  - "+ propertyBathroomCount +") - ((max_bathrooms - min_bathrooms)/2)" +
                        ") as bathroom_count_diff " +
                    "FROM requirement " +
                    "HAVING " +
                        "distance < "+ MAX_DISTANCE_IN_MILES +" and " +
                        "price_diff_ratio <= "+ BUDGET_DIFF_TOTAL_STEPS +" and " +
                        "bedroom_count_diff <= "+ MAX_RANGE_OF_BEDROOM +" and " +
                        "bathroom_count_diff <= "+ MAX_RANGE_OF_BATHROOM +
                        ( isCountQuery ? " and " + getTotalPercentQuery() + " >= " + MINIMUM_SUCCESSFUL_MATCH_PERCENT : "") +
                ") AS temp ";
    }

    private static String getTotalPercentQuery() {
        return "(" +
                "ROUND(( "+ DISTANCE_STEPS_COUNT +" - FLOOR( distance / " + DISTANCE_STEP_IN_MILES + " )) * " + DISTANCE_PERCENT_STEP + ") + " +
                "ROUND((" + BUDGET_DIFF_TOTAL_STEPS + " - ABS(price_diff_ratio)) * " + BUDGET_DIFF_PERCENT_STEP + ") + " +
                "ROUND((1 - (GREATEST(bedroom_count_diff, 0) / " + BEDROOM_DIFF_PERCENT_TOTAL_STEPS + ")) * " + BEDROOM_WEIGHTAGE_PERCENT + ") + " +
                "ROUND((1 - (GREATEST(bathroom_count_diff, 0) / " + BATHROOM_DIFF_PERCENT_TOTAL_STEPS + ")) * " + BATHROOM_WEIGHTAGE_PERCENT + ")" +
            ") ";
    }
}
