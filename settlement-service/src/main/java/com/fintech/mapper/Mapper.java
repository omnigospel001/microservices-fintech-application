package com.fintech.mapper;

import com.fintech.entity.Settlement;
import com.fintech.response.SettlementResponse;
import org.springframework.stereotype.Service;

@Service
public class Mapper {

    public SettlementResponse toResponse(Settlement settlement) {
        if (settlement == null) {
            return null;
        }
        return new SettlementResponse(
                settlement.getId(),
                settlement.getTransactionType(),
                settlement.getReferenceId(),
                settlement.getUserId(),
                settlement.getAmount(),
                settlement.getStatus(),
                settlement.getCreatedAt(),
                settlement.getSettledAt()
        );
    }

}
