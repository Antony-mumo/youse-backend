package com.company.youse.models.yousepay;

import com.company.youse.enums.AccountType;
import com.company.youse.enums.PostingType;
import com.company.youse.models.PO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="gl_account")
public class GlAccount extends PO {

	private String accountNo;
	private String accountName;
	private String description;
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	private String accountClass;
	@Enumerated(EnumType.STRING)
	private PostingType naturalPosting;

	@Override
	public boolean equals(Object obj) {
		GlAccount other = (GlAccount)obj;
		
		if(other.accountNo.equals(getAccountNo())) {
			return true;
		}

		return super.equals(obj);
	}

    public void copy(GlAccount anAccount) {
		setAccountName(anAccount.getAccountName());
		setDescription(anAccount.getDescription());
		setAccountClass(anAccount.getAccountClass());
    }
}
