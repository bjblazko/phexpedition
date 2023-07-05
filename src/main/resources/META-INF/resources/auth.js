let config = {
    authority: document.getElementsByTagName("auth-authority").item(0).textContent,
    clientId: document.getElementsByTagName("auth-client-id").item(0).textContent,
    redirectUri: document.getElementsByTagName("auth-redirect-uri").item(0).textContent,
    baseUrl: document.getElementsByTagName("base-url").item(0).textContent,
    responseType: 'token id_token',
    scope: 'openid profile email',
}


let url = new URL(window.location.href)
let params = new URLSearchParams(url.hash.substr(1))

let accessToken = params.get("access_token")
let tokenType = params.get("token_type");
let expiresIn = params.get("expires_in");
let scope = params.get("scope");
let idToken = params.get("id_token");
let state = params.get("state");

if (accessToken) {
    document.getElementById('loginButton').style.display = 'none'
    document.getElementById('userInfo').style.display = 'block'
    document.getElementById('userInfo').style.visibility = "visible"

    let jwtPayload = decodeJwtPayload(idToken)

    document.getElementById('userName').innerText = jwtPayload.name
    document.getElementById('userMail').innerText = jwtPayload.email

    //console.log(idToken)
    api(idToken)
    apiUser(idToken)

} else {
    document.getElementById('loginButton').addEventListener('click', function () {
        window.location.href = `${config.authority}?` +
            `response_type=${encodeURIComponent(config.responseType)}&` +
            `client_id=${config.clientId}&` +
            `redirect_uri=${config.redirectUri}&` +
            `scope=${encodeURIComponent(config.scope)}&` +
            `state=huepattl-${Date.now()}&` +
            `nonce=huepattl-nonce-${Date.now()}`
    })
}

function decodeJwtPayload(jwt) {

    // Split the JWT into its three parts: header, payload, and signature
    const jwtParts = jwt.split(".");
    const jwtHeader = jwtParts[0];
    const jwtPayload = jwtParts[1];
    const jwtSignature = jwtParts[2];

    // Decode the JWT header and payload using Base64Url decoding
    const decodedJwtHeader = JSON.parse(atob(jwtHeader.replace(/-/g, "+").replace(/_/g, "/")));
    const decodedJwtPayload = JSON.parse(atob(jwtPayload.replace(/-/g, "+").replace(/_/g, "/")));

    // Log the decoded JWT header and payload to the console
    console.log("Decoded JWT Payload:", decodedJwtHeader);
    console.log("Decoded JWT Payload:", decodedJwtPayload);
    return decodedJwtPayload
}

function api(idToken) {
    let apiUrl = `${config.baseUrl}/api2`//"https://api-yr4f3nxarq-ew.a.run.app"

    fetch(apiUrl, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${idToken}`,
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    }).then((response) => response.json())
        .then((data) => {
            console.log(data)
            console.log(`Name: ${data.jwt}`)
        })
}

function apiUser(idToken) {
    let apiUrl = `${config.baseUrl}/api/user/_me`
    fetch(apiUrl, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${idToken}`,
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    }).then((response) => response.json())
        .then((user) => {
            console.log(user)
            console.log(`Name: ${user.displayName}`)
            alert(`User: ${user.displayName}\nMail: ${user.email}`)
        })

}