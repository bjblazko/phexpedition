# Phexpedition

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=coverage)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=bjblazko_phexpedition&metric=bugs)](https://sonarcloud.io/summary/new_code?id=bjblazko_phexpedition)

## Setting up Google Cloud project

Phexpedition requires Google Cloud to run. Initially setting up your project
and your GitHub Actions CI/CD pipeline in described in the seperate [setup document](.github/workflows/README.md).

## Local setup

Since we require Google Cloud resources, you still need to have a cloud project set up.
See previous chapter for details. Once you have at least a single test project running,
make sure to declare these environment variables. You might want to use a `.env` file
for this:

```shell

AUTH_REDIRECT_URI=http://localhost:8080
AUTH_CLIENT_ID=<client ID from APIs and services -> Credentials -> OAuth 2.0 Client IDs>
AUTH_CLIENT_SECRET=<client secret from APIs and services -> Credentials -> OAuth 2.0 Client IDs>
GOOGLE_APPLICATION_CREDENTIALS=</path/to/IAM/JSON/key>
```

Explaination:

- `AUTH_REDIRECT_URI`: This app's base URL that Google Authentication will invoke after successful authentication. 
  You need to enable test users in [APIs and services -> OAuth consent screen](https://console.cloud.google.com/apis/credentials/consent) and
  need to enable "Authorised JavaScript origins" and "Authorised redirect URIs" in [APIs and services -> Credentials -> OAuth 2.0 Client IDs](https://console.cloud.google.com/apis/credentials)
  by adding "http://localhost:8080" each
- `AUTH_CLIENT_ID`: client ID you have configured in [APIs and services -> Credentials -> OAuth 2.0 Client IDs](https://console.cloud.google.com/apis/credentials)
- `AUTH_CLIENT_SECRET`: client secret to be found in the configured client secret [APIs and services -> Credentials -> OAuth 2.0 Client IDs](https://console.cloud.google.com/apis/credentials)
- `GOOGLE_APPLICATION_CREDENTIALS`: fully qualified path pointing to the JSON key of the app's IAM service account