/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.register.user;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an user contract fetched from the blockchain.
 * An user contract is composed by a provider name, a provider address, an user address, a bitmask, a revoke address and
 * a revoke tx id
 */
public class UserChannel implements Serializable, Comparable<Object> {

    private static final long serialVersionUID = 1L;
    private String providerName;
    private String providerAddress;
    private String userAddress;
    private String bitmask;
    private String revokeAddress;
    private String revokeTxId;
    private long since;
    private long until;
    private String path;
    private String providerXpub;
    private String providerTpub;

    /**
     * Creates an empty instance
     */
    public UserChannel() {
        // DO NOTHING
    }

    /**
     * Creates an instance from provider name, provider address, user address and bitmask.
     *
     * @param providerName    the name of the provider
     * @param providerAddress the address of the provider
     * @param userAddress     the address of the user
     * @param bitmask         the string representing the bitmask
     */
    public UserChannel(String providerName, String providerAddress, String userAddress, String bitmask) {
        this.providerName = providerName;
        this.providerAddress = providerAddress;
        this.userAddress = userAddress;
        this.bitmask = bitmask;
    }

    public UserChannel(String providerName, String providerAddress, String userAddress, String bitmask, String path) {

        this(providerName, providerAddress, userAddress, bitmask);

        this.path = path;
    }

    /**
     * Set the provider name
     * @param providerName the provider name
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    /**
     * Retrieve the provider name
     * @return the provider name
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * Set the provider address
     * @param providerAddress the provider address
     */
    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    /**
     * Retrieve the provider address
     * @return the provider address
     */
    public String getProviderAddress() {
        return providerAddress;
    }

    /**
     * Set the user address
     * @param userAddress the user address
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     * Retrieve the user address
     * @return the user address
     */
    public String getUserAddress() {
        return userAddress;
    }

    /**
     * Retrieve the bitmask
     * @return the bitmask
     */
    public String getBitmask() {
        return bitmask;
    }

    /**
     * Set the bitmask
     * @param bitmask the bitmask
     */
    public void setBitmask(String bitmask) {
        this.bitmask = bitmask;
    }

    /**
     * Retrieve the revoke address
     * @return the revoke address
     */
    public String getRevokeAddress() {
        return revokeAddress;
    }

    /**
     * Set the revoke address
     * @param revokeAddress the revoke address
     */
    public void setRevokeAddress(String revokeAddress) {
        this.revokeAddress = revokeAddress;
    }

    /**
     * Set the revoke transaction id
     * @param revokeTxId the revoke transaction id
     */
    public void setRevokeTxId(String revokeTxId) {
        this.revokeTxId = revokeTxId;
    }

    /**
     * Retrieve the revoke transaction id
     * @return the revoke transaction id
     */
    public String getRevokeTxId() {
        return revokeTxId;
    }

    public long getSince() {
        return since;
    }

    public void setSince(long since) {
        this.since = since;
    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProviderXpub() {
        return providerXpub;
    }

    public void setProviderXpub(String providerXpub) {
        this.providerXpub = providerXpub;
    }

    public String getProviderTpub() {
        return providerTpub;
    }

    public void setProviderTpub(String providerTpub) {
        this.providerTpub = providerTpub;
    }

    public boolean isValid() {

        return (System.currentTimeMillis() >= since) && (System.currentTimeMillis() <= until);

    }

    @Override
    public String toString() {
        return "UserChannel{" +
                "providerName='" + providerName + '\'' +
                ", providerAddress='" + providerAddress + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", bitmask='" + bitmask + '\'' +
                ", revokeAddress='" + revokeAddress + '\'' +
                ", revokeTxId='" + revokeTxId + '\'' +
                ", since=" + since +
                ", until=" + until +
                ", path='" + path + '\'' +
                ", providerXpub='" + providerXpub + '\'' +
                ", providerTpub='" + providerTpub + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof UserChannel))
            return false;

        if (this == object)
            return true;

        UserChannel userChannel = (UserChannel) object;

        return Objects.equals(providerName, userChannel.providerName) &&
                Objects.equals(providerAddress, userChannel.providerAddress) &&
                Objects.equals(userAddress, userChannel.userAddress) &&
                Objects.equals(bitmask, userChannel.bitmask) &&
                Objects.equals(revokeAddress, userChannel.revokeAddress) &&
                Objects.equals(revokeTxId, userChannel.revokeTxId) &&
                since == userChannel.since &&
                until == userChannel.until &&
                Objects.equals(path, userChannel.path) &&
                Objects.equals(providerXpub, userChannel.providerXpub) &&
                Objects.equals(providerTpub, userChannel.providerTpub);
    }

    @Override
    public int hashCode() {

        return Objects.hash(providerName, providerAddress, userAddress, bitmask, revokeAddress, revokeTxId, since, until, path, providerXpub, providerTpub);

    }

    @Override
    public int compareTo(Object object) {

        UserChannel userChannel = (UserChannel) object;

        return compareStrings(providerName, userChannel.getProviderName());
    }

    private static int compareStrings(String first, String second) {
        final int BEFORE = -1;
        final int EQUALS = 0;
        final int AFTER = 1;

        if (first == null && second == null) {

            return EQUALS;

        } else if (first == null && second != null) {

            return BEFORE;

        } else if (first != null && second != null) {

            return first.compareTo(second);

        } else /* if (first != null && second == null) */ {

            return AFTER;
        }

    }

}