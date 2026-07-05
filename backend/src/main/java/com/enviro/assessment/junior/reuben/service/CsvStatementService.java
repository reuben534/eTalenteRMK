package com.enviro.assessment.junior.reuben.service;

import com.enviro.assessment.junior.reuben.domain.WithdrawalNotice;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CsvStatementService {

    public String build(List<WithdrawalNotice> notices) {
        StringBuilder csv = new StringBuilder();
        csv.append("noticeId,createdAt,investor,product,productType,amount,balanceBefore,balanceAfter,status\n");
        for (WithdrawalNotice notice : notices) {
            csv.append(notice.getId()).append(',')
                    .append(notice.getCreatedAt()).append(',')
                    .append(escape(notice.getInvestor().getFirstName() + " " + notice.getInvestor().getLastName())).append(',')
                    .append(escape(notice.getProduct().getName())).append(',')
                    .append(notice.getProduct().getType()).append(',')
                    .append(notice.getAmount()).append(',')
                    .append(notice.getBalanceBefore()).append(',')
                    .append(notice.getBalanceAfter()).append(',')
                    .append(notice.getStatus()).append('\n');
        }
        return csv.toString();
    }

    private String escape(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
