
Setting up development environment
==================================
* Run api server

        ../mvnw spring-boot:run
        
* Generate JWT token from https://jwt.io using following information (sample 
[Token in jwt.io](https://jwt.io/#debugger-io?token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImtleTEifQ.eyJVc2VySWQiOiIxMjMiLCJBY2NJZCI6IjEiLCJPcmdJZCI6IjIiLCJQZXJtaXNzaW9ucyI6IjMxIiwiaWF0IjoxNTE2MjM5MDIyfQ.DmEvO7fRe_hYA5ny-aTVoVmMTcl5CfQwrVhOteZCtWn0gsSBapUBtqLAfJ7KsY0EzTLZ3GKG2bEHbqNGGaAjImR_HCXKUuJ564YMDs4c5X-7bxor1Ncs3cCKv8LS058cJmgpJG2Ci6ppqzihmpt_cabzFWflewHNtI8g2IT6aPm-NUpBEPXlBAtv1I1nOXj7PzSYw-nmUeYy4aSkBfEnUzHaQ57nFyGfUNwn0Ptvn_7kx3-ily75TUnPKj7qHtz0w6tiAa8XZ7ZNdGLgR8N58zosy2pfH618YIzYO_fCO1jCqt5oEZJjNre6uqI7DA2WAIz2HFfOCrbjsxWTwEZ0xw&publicKey=-----BEGIN%20PUBLIC%20KEY-----%0AMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqq9huwc3u0EVRAhESEb%2B%0A8CFxO8TEpbw%2Fgi%2FXDAQSCdHRyuPxiNyWLNrJ%2FQbq2lcueSdSc94vUVJ2mmPIRMX5%0AIWDLEbqjPgRaNM26VroGZgT1HKOWLF2Lra%2F6bwxhH9RhG0lCwIaXLqp5zPXt3gia%0AOi8OIoKhUTEdhkZe2Lw%2F9PyZj4ky%2Fgy4gf4nrJ5yasVBNHokEmvixb3XlQVTjYs6%0A3m9MVNqGgV2KTgTnfBA24H%2FXQftKas%2BDn8yDrtUDmKr87Xo0eI5FeM5RxLmFQezI%0ArobHgH24U3ijfZZkPb6XT9XarcuccLSrUD1SZTmqxdA5mspmwsost%2FN8bVp0ouJp%0AGQIDAQAB%0A-----END%20PUBLIC%20KEY-----))
    * Header: 
    
            {
              "alg": "RS256",
              "typ": "JWT",
              "kid": "authz1"
            }
            
    * Payload

              {
                "iss": "iam-service",
                "iat": 1516239022,
                "exp": 1716239322,
                "user_id": "1",
                "product": "freshdesk",
                "account_id": "2", // product account ID
                "account_domain": "tmkarthi.freshdesk.com", // produt API domain
                "org_id": "3",
                "privileges": "2047",
                "type": "agent", // other possible value "contact"
                "org_user_id": "4" // FreshID agent_id for agent. Once, MCR becomes available, MCR user_id for contact. Till that time, null for contact
              }
      * RSASHA256: The authz1 private/public key pair from `./src/test/resources/authz-key-pair` directory
    
* Add a todo

        curl -X POST \
          http://localhost:8080/api/v1/todos \
          -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImF1dGh6MSJ9.eyJpc3MiOiJpYW0tc2VydmljZSIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNzE2MjM5MzIyLCJ1c2VyX2lkIjoiMSIsInByb2R1Y3QiOiJmcmVzaGRlc2siLCJhY2NvdW50X2lkIjoiMiIsImFjY291bnRfZG9tYWluIjoidG1rYXJ0aGkuZnJlc2hkZXNrLmNvbSIsIm9yZ19pZCI6IjMiLCJwcml2aWxlZ2VzIjoiMjA0NyIsInR5cGUiOiJhZ2VudCIsIm9yZ191c2VyX2lkIjoiNCJ9.fbduTB5ajbtRSoOxnoxnQKPUqWimHLId9nuJEZiQkOnj02gMFW4Lm1tGD37o1ZckjCJkbUdANiZNphhiYKXSGLLiPl6tefRBb6ADUqrV2HUj7OsoEZ5sHv0xx3Y5xRYHumCXCliVFsMFbXAmQ-U2jaUJhKEDtq-SONRUAqNI0eDi7PFSHkjyZm3ens_ayj1CcXm6Jdf-vR-pyxX9K1ZGjv0kCdSX2kOKoE121Crd4cPt-_bN8mTIZtO2iYX3RHS31AK3jHCrIxDYG0JgJITFnDitESH4TE9jmfahK6W_HgcCGLSlfgTc4Wk7idUXCF3G3rIf2Qr76W00meQTsVJJ7Q' \
          -H 'Content-Type: application/json' \
          -H 'X-FWI-Client-ID: service1' \
          -d '{
        	"title": "Buy milk"
        }'

* Fetch all todos

        curl -X GET \
          http://localhost:8080/api/v1/todos \
          -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImF1dGh6MSJ9.eyJpc3MiOiJpYW0tc2VydmljZSIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNzE2MjM5MzIyLCJ1c2VyX2lkIjoiMSIsInByb2R1Y3QiOiJmcmVzaGRlc2siLCJhY2NvdW50X2lkIjoiMiIsImFjY291bnRfZG9tYWluIjoidG1rYXJ0aGkuZnJlc2hkZXNrLmNvbSIsIm9yZ19pZCI6IjMiLCJwcml2aWxlZ2VzIjoiMjA0NyIsInR5cGUiOiJhZ2VudCIsIm9yZ191c2VyX2lkIjoiNCJ9.fbduTB5ajbtRSoOxnoxnQKPUqWimHLId9nuJEZiQkOnj02gMFW4Lm1tGD37o1ZckjCJkbUdANiZNphhiYKXSGLLiPl6tefRBb6ADUqrV2HUj7OsoEZ5sHv0xx3Y5xRYHumCXCliVFsMFbXAmQ-U2jaUJhKEDtq-SONRUAqNI0eDi7PFSHkjyZm3ens_ayj1CcXm6Jdf-vR-pyxX9K1ZGjv0kCdSX2kOKoE121Crd4cPt-_bN8mTIZtO2iYX3RHS31AK3jHCrIxDYG0JgJITFnDitESH4TE9jmfahK6W_HgcCGLSlfgTc4Wk7idUXCF3G3rIf2Qr76W00meQTsVJJ7Q' \
          -H 'X-FWI-Client-ID: service1' \
          -H 'Content-Type: application/json'

Useful commands
===============
* Build with tests

        ../mvnw clean build

Setting up authentication of JWT tokens
=====================================
 
* Generate public/private key pair for signing JWT token (once AuthZ is live, it should provide these pairs).
For development, the key pair present in `./src/test/resources/authz-key-pair` can be used.

        $ openssl genpkey -algorithm RSA -out authz1_private.pem -pkeyopt rsa_keygen_bits:2048
        $ openssl rsa -in authz1_private.pem -pubout -out authz1_public.pem

* Set the public key in application-dev.properties for `security.jwt.authzPublicKeys.newKeyId`. The `newKeyId` should be set as
 the `kid` in the JWT header. 