package com.company.youse.models.yousepay;

import com.company.youse.models.PO;
import com.company.youse.platform.data.Transferable;
import com.company.youse.pojo.SysAccountDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "u_sys_accounts")
public class SysAccount extends PO implements Transferable<SysAccountDTO> {

    public static final String CLASSNAME = "SysAccount";

    @Column(name = "org_name")
    private String orgName;
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "contact_email")
    private String contactEmail;
    @Column(name = "contact_phone")
    private String contactPhone;

    @OneToMany(mappedBy = "sysAccount", orphanRemoval = true, cascade = {
            CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH
            ,CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<OrgShortCode> orgShortCodes;

    @Override
    public SysAccountDTO toDTO() {
        var sysAccount = new SysAccountDTO();
        sysAccount.setContactEmail(getContactEmail());
        sysAccount.setContactPhone(getContactPhone());
        sysAccount.setMemberId(getMemberId());
        sysAccount.setOrgName(getOrgName());
        sysAccount.setOrgShortCode(Objects.isNull(orgShortCodes) ? new ArrayList<>() :
                orgShortCodes.stream()
                .map(OrgShortCode::toDTO)
                .collect(Collectors.toList()));

        return sysAccount;
    }
}
