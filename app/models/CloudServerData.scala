package models

/**
 * Created by ThomasWorkBook on 18/04/15.
 * case class for holding the data for the form for creating a digital ocean droplet
 */
case class CloudServerData(name: String,
                            region: String,
                            size: String,
                            backUps: Boolean,
                            ipv6: Boolean,
                            ssh: Boolean,
                            sshName: Option[String],
                            sshPublicKey: Option[String])

