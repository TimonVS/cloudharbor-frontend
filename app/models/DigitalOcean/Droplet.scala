package models.DigitalOcean

import play.api.libs.json.Json

/**
 * Created by Rudie on 3-5-2015.
 */
case class Droplet(name: String, image: String, region: String, size: String, backups: Boolean, ipv6: Boolean, sshKeys: Option[List[BigDecimal]]) {
  def toJson = Json.obj(
    "name" -> name,
    "image" -> image,
    "region" -> region,
    "size" -> size,
    "backups" -> backups,
    "ipv6" -> ipv6,
    "ssh_keys" -> Json.arr(sshKeys)
  )
}
