package net.nikonorov.lazerninja

/**
 * Created by vitaly on 28.02.16.
 */

class UserProfile( val username: String,
                  val email: String,
                  val first_name: String,
                  val last_name: String,
                  val userpic: String){

    constructor() : this("", "", "", "", "")

}