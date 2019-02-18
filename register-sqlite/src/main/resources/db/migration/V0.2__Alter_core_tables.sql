ALTER TABLE provider_channel
  ADD COLUMN user_xpub text;

ALTER TABLE provider_channel
  ADD COLUMN user_tpub text;

ALTER TABLE user_channel
  ADD COLUMN provider_xpub text;

ALTER TABLE user_channel
  ADD COLUMN provider_tpub text;