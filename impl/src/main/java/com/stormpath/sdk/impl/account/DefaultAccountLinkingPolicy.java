package com.stormpath.sdk.impl.account;

import com.stormpath.sdk.account.AccountLinkingPolicy;
import com.stormpath.sdk.account.AccountLinkingStatus;
import com.stormpath.sdk.account.AutomaticProvisioningStatus;
import com.stormpath.sdk.account.MatchingProperty;
import com.stormpath.sdk.impl.ds.InternalDataStore;
import com.stormpath.sdk.impl.resource.*;
import com.stormpath.sdk.lang.Assert;
import com.stormpath.sdk.tenant.Tenant;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 1.1.0
 */
public class DefaultAccountLinkingPolicy extends AbstractInstanceResource implements AccountLinkingPolicy {

    // ENUM PROPERTIES
    static final EnumProperty<AccountLinkingStatus> STATUS = new EnumProperty<>(AccountLinkingStatus.class);
    static final EnumProperty<AutomaticProvisioningStatus> AUTOMATIC_PROVISIONING = new EnumProperty<>("automaticProvisioning", AutomaticProvisioningStatus.class);
    static final EnumProperty<MatchingProperty> MATCHING_PROPERTY = new EnumProperty<>("matchingProperty", MatchingProperty.class);


    // INSTANCE RESOURCE REFERENCES:
    static final ResourceReference<Tenant> TENANT = new ResourceReference<Tenant>("tenant", Tenant.class);

    private static final Map<String, Property> PROPERTY_DESCRIPTORS = createPropertyDescriptorMap(
            STATUS, AUTOMATIC_PROVISIONING, MATCHING_PROPERTY, TENANT);

    @Override
    public Map<String, Property> getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }

    public DefaultAccountLinkingPolicy(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultAccountLinkingPolicy(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    protected static Map<String, Property> createPropertyDescriptorMap(Property... props) {
        Map<String, Property> m = new LinkedHashMap<String, Property>();
        for (Property prop : props) {
            m.put(prop.getName(), prop);
        }
        return m;
    }


    @Override
    public AccountLinkingStatus getStatus() {
        String value = getStringProperty(STATUS.getName());
        if (value == null) {
            return null;
        }
        return AccountLinkingStatus.valueOf(value);
    }

    @Override
    public AccountLinkingPolicy setStatus(AccountLinkingStatus status) {
        Assert.notNull(status, "status cannot be null.");
        setProperty(STATUS, status.name());
        return this;
    }

    @Override
    public AutomaticProvisioningStatus getAutomaticProvisioning() {
        String value = getStringProperty(AUTOMATIC_PROVISIONING.getName());
        if (value == null) {
            return null;
        }
        return AutomaticProvisioningStatus.valueOf(value);
    }

    @Override
    public AccountLinkingPolicy setAutomaticProvisioning(AutomaticProvisioningStatus automaticProvisioningStatus) {
        Assert.notNull(automaticProvisioningStatus, "automaticProvisioning cannot be null.");
        setProperty(AUTOMATIC_PROVISIONING, automaticProvisioningStatus.name());
        return this;
    }

    @Override
    public MatchingProperty getMatchingProperty() {
        String value = getStringProperty(MATCHING_PROPERTY.getName());
        if (value == null) {
            return null;
        }
        return MatchingProperty.valueOf(value);
    }

    @Override
    public AccountLinkingPolicy setMatchingProperty(MatchingProperty matchingProperty) {
        if(matchingProperty == null) {
            writeLock.lock();
            try {
                this.deletedPropertyNames.add(MATCHING_PROPERTY.getName()); // To tell the DataStore that this property is nullable
            } finally {
                writeLock.unlock();
            }
        }
        String mp = matchingProperty == null ? null : matchingProperty.name();
        setProperty(MATCHING_PROPERTY, mp);
        return this;
    }

    @Override
    public Tenant getTenant() {
        return getResourceProperty(TENANT);
    }

    @Override
    public boolean isAccountLinkingEnabled () {
        return AccountLinkingStatus.ENABLED.equals(getStatus()) ? true : false;
    }

    @Override
    public boolean isAutomaticProvisioningEnabled () {
        return AutomaticProvisioningStatus.ENABLED.equals(getAutomaticProvisioning()) ? true : false;
    }
}
