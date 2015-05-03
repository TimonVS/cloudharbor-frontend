package models.DigitalOcean

import play.api.libs.json.Json

/**
 * Created by Rudie on 3-5-2015.
 */
case class SSHKey(name: String, publicKey: String) {
  def toJson = Json.obj(
    "name" -> name,
    "public_key" -> publicKey
  )
}
