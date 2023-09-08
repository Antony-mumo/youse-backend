package com.company.youse.models.yousepay;

import com.company.youse.enums.*;
import com.company.youse.models.PO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="gl_factacc")
public class FactAcc extends PO {

	private String accountNo;
	private String accountName;
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	private String accountClass;
	private Date postingDate;
	private String description;
	@Enumerated(EnumType.STRING)
	private Currency currency;
	private BigDecimal amount;
	@Enumerated(EnumType.STRING)
	private PostingType postingType;
	private String referencedFactId;
	private String transactionId; //Transaction id, should be system generated to ensure uniqueness
	private String transactionReference; //User entered value
	private String customerRefId;
	private String merchantRefId;
	@Enumerated(EnumType.STRING)
	private TransactionChannel channel;
	private String paymentProvider;
	@Enumerated(EnumType.STRING)
	private AccountEvent event = AccountEvent.ACC_POST;
	
	@Transient
	private Double runningTotal;
	
	@Transient
	private Double signedAmount;
	
	@Transient
	private Long sequenceId;
}
