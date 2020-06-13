package com.nimesh.real_estate_ms.repository.impl;

import com.nimesh.real_estate_ms.entity.Property;
import com.nimesh.real_estate_ms.entity.Requirement;
import com.nimesh.real_estate_ms.repository.PropertyCustomRepository;
import com.nimesh.real_estate_ms.repository.util.CustomPage;
import com.nimesh.real_estate_ms.service.helper.CommonHelper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static com.nimesh.real_estate_ms.config.AppConstants.*;

public class PropertyCustomRepositoryImpl implements PropertyCustomRepository {

    private static final double DISTANCE_STEPS_COUNT = MAX_DISTANCE_IN_MILES/DISTANCE_STEP_IN_MILES;
    private static final double DISTANCE_PERCENT_STEP = DISTANCE_WEIGHTAGE_PERCENT/DISTANCE_STEPS_COUNT;
    private static final double BUDGET_MULTIPLIER_CONSTANT = (1/MAX_PRICE_RANGE_RATIO) * (BUDGET_DIFF_TOTAL_STEPS - 1);
    private static final double BUDGET_DIFF_PERCENT_STEP = BUDGET_WEIGHTAGE_PERCENT/ BUDGET_DIFF_TOTAL_STEPS;
    private static final int BEDROOM_DIFF_PERCENT_TOTAL_STEPS = MAX_RANGE_OF_BEDROOM + 1;
    private static final int BATHROOM_DIFF_PERCENT_TOTAL_STEPS = MAX_RANGE_OF_BATHROOM + 1;

    @PersistenceContext
    private EntityManager em;

    @Override
    public CustomPage<Property> findAllByRequirement(Requirement requirement, long offset, int pageSize) {
        String queryString =
            "SELECT *, " +
                getTotalPercentQuery() + " as match_percent " +
            getInnerFromQuery(requirement, false) +
            "HAVING match_percent >= "+ MINIMUM_SUCCESSFUL_MATCH_PERCENT + " " +
            "ORDER BY match_percent DESC " +
            "LIMIT "+ offset +", "+ pageSize;
        List<Property> properties = em.createNativeQuery(queryString, Property.class).getResultList();
        long count = getCountByRequirement(requirement);
        return new CustomPage<>(properties, offset, pageSize, count);
    }

    private long getCountByRequirement(Requirement requirement) {
        String countQuery = "SELECT count(*) " + getInnerFromQuery(requirement, true);
        return ((Number) em.createNativeQuery(countQuery).getSingleResult()).longValue();
    }

    private static String getInnerFromQuery(Requirement requirement, boolean isCountQuery) {
        Double requirementLatitude = requirement.getLatitude();
        Double requirementLongitude = requirement.getLongitude();
        Double meanBudget = requirement.getMeanBudget();
        Double rangeBudget = requirement.getRangeBudget();
        Double meanBedrooms = CommonHelper.getMean(requirement.getMinBedrooms(),requirement.getMaxBedrooms());
        Double rangeBedrooms = CommonHelper.getRange(requirement.getMinBedrooms(), requirement.getMaxBedrooms());
        Double meanBathrooms = CommonHelper.getMean((double)requirement.getMinBathrooms(),(double)requirement.getMaxBathrooms());
        Double rangeBathrooms = CommonHelper.getRange((double)requirement.getMinBathrooms(),(double)requirement.getMaxBathrooms());
        return "FROM (" +
                    "SELECT " +
                        "*, " +
                        "(" +
                            MILES_DISTANCE_CONSTANT +
                            " * acos (" +
                                " cos ( radians("+ requirementLatitude +") )" +
                                " * cos( radians( latitude ) )" +
                                " * cos( radians( longitude ) - radians("+ requirementLongitude +") )" +
                                " + sin ( radians("+ requirementLatitude +") )" +
                                " * sin( radians( latitude ) )" +
                            ")" +
                        ") AS distance, " +
                        "(" +
                            "CEILING( " +
                                "(" +
                                    BUDGET_MULTIPLIER_CONSTANT + " * " +
                                    "(" +
                                        "ABS( "+ meanBudget +" - price) - " + rangeBudget +
                                    ")" +
                                ") / " + meanBudget +
                            ")" +
                        ") as price_diff_ratio, " +
                        "(" +
                            "ABS("+ meanBedrooms +" - bedroom_count) - " + rangeBedrooms +
                        ") as bedroom_count_diff, " +
                        "(" +
                            "ABS("+ meanBathrooms +" - bathroom_count) - " + rangeBathrooms +
                        ") as bathroom_count_diff " +
                    "FROM property " +
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
