# Keycloak

## Export realm

realm can be exported using the following steps:

* Login as the administrator
* Choose the desired realm
* Navigate to `Realm settings`
* In the upper-right corner click on Action dropdown
* Select `Partial export` and select all checkboxes

The export contains all the realm info besides sensitive content, for us it means it lacks:

* Users
* OAuth identity provider secrets

