/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.register.provider;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a provider contract fetched from the blockchain.
 * A provider contract is composed by a provider address, an user address, a revoke address, a bitmask, and
 * a revoke tx id and a creation time.
 */
public class ProviderChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String providerAddress;
    private String userAddress;
    private String bitmask;
    private String revokeAddress;
    private String revokeTxId;
    private long creationTime;
    private long since;
    private long until;
    private String path;
    private String userXpub;
    private String userTpub;


    /**
     * Creates an empty instance
     */
    public ProviderChannel() {
        // DO NOTHING
    }

    /**
     * Creates an instance from provider address, user address and bitmask.
     *
     * @param providerAddress the address of the provider
     * @param userAddress     the address of the user
     * @param bitmask         the string representing the bitmask
     */
    public ProviderChannel(String providerAddress, String userAddress, String bitmask) {
        this.providerAddress = providerAddress;
        this.userAddress = userAddress;
        this.bitmask = bitmask;
    }

    /**
     * Set the provider address
     * @param providerAddress the provider address
     */
    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    /**
     * Returns the provider address
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
     * Returns the user address
     * @return the user address
     */
    public String getUserAddress() {
        return userAddress;
    }

    /**
     * Returns the bitmask
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
     * Return the revoke address
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
     * @param revokeTxId revoke transaction id
     */
    public void setRevokeTxId(String revokeTxId) {
        this.revokeTxId = revokeTxId;
    }

    /**
     * Returns the revoke transaction id
     * @return the revoke transaction id
     */
    public String getRevokeTxId() {
        return revokeTxId;
    }

    /**
     * Returns the creation time
     * @return the creation time
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Set the creation time
     * @param creationTime the creation time
     */
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
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

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getUserXpub() {
        return userXpub;
    }

    public void setUserXpub(String userXpub) {
        this.userXpub = userXpub;
    }

    public String getUserTpub() {
        return userTpub;
    }

    public void setUserTpub(String userTpub) {
        this.userTpub = userTpub;
    }

    public boolean isValid() {
        return (System.currentTimeMillis() >= since) && (System.currentTimeMillis() <= until);
    }

    @Override
    public String toString() {
        return "ProviderChannel{" +
                "providerAddress='" + providerAddress + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", revokeAddress='" + revokeAddress + '\'' +
                ", bitmask='" + bitmask + '\'' +
                ", revokeTxId='" + revokeTxId + '\'' +
                ", creationTime=" + creationTime +
                ", since=" + since +
                ", until=" + until +
                ", path='" + path + '\'' +
                ", userXpub='" + userXpub + '\'' +
                ", userTpub='" + userTpub + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof ProviderChannel))
            return false;

        if (this == object)
            return true;

        ProviderChannel providerChannel = (ProviderChannel) object;

        return Objects.equals(providerAddress, providerChannel.providerAddress) &&
                Objects.equals(userAddress, providerChannel.userAddress) &&
                Objects.equals(revokeAddress, providerChannel.revokeAddress) &&
                Objects.equals(bitmask, providerChannel.bitmask) &&
                Objects.equals(revokeTxId, providerChannel.revokeTxId) &&
                creationTime == providerChannel.creationTime &&
                since == providerChannel.since &&
                until == providerChannel.until &&
                Objects.equals(path, providerChannel.path) &&
                Objects.equals(userXpub, providerChannel.userXpub) &&
                Objects.equals(userTpub, providerChannel.userTpub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerAddress, userAddress, revokeAddress, bitmask, revokeTxId, creationTime, since, until, path, userXpub, userTpub);
    }

}