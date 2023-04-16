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

Phexpedition requires Google Cloud to run. This means we assume:

- you have basic knowledge about Google Cloud, at least in these areas:
  - Service Accounts (SA) in Identity and Access Management (IAM)
  - Google Cloud Run
- you already have set up a Google Cloud project for these stages (or modify CI/CD pileine accordingly):
  - `Test`: Stage for non-local development and testing
  - `Prod`: for production and beta stages

If not stated otherwise, we assume the following instructions to be followed per stage.

We employ these technologies, products or facilities in order to build and operate Phexpedition:

- Github
  - Git Source Control Management (SCM)
  - Github Actions for:
    - building sources using Java and Maven 
    - unit testing using Java and maven
    - setting up _some_ Google Cloud resources with `gcloud` CLI (no Terraform yet)
    - deploying artifacts to Google Cloud
  - Github Environments (e.g. `test` and `production` with dedicated variables and secrets per stage)
- Google Cloud
  - Cloud Build triggered by Github actions (using Build Packs)
  - Cloud Run for the application itself
  - several related and required facilities like IAM etc.

### Manual steps

#### Create Google Cloud project (per stage)

When having logged in to [Google Cloud Console](https://console.cloud.google.com),
make sure to create a new project per stage. Unless you disabled or edit Github Actions Environments and workflow, these are currently:

- `phexpedition-test`: development and testing
- `phexpedition`: production and beta


#### Create CI/CD service account (per stage)

Go to *IAM* and create a new service account to be used by Github Actions
for further setting up your project and doing CI/CD:

- _Service account Name_: `github`
- _Service account ID_: `github`
- _Service account description_: `Github CI service account`
- Roles:
  - `Cloud Deploy Admin`
  - `Service Usage Admin`
  - `Service Account Admin`


#### Setup Github Actions Environment (per stage)

In you repository fork, go to _Settings_ &rarr; _Environments_ &rarr; _New environment_ (or choose existing one).
Then add these secrets:

* `GOOGLE_PROJECT`: the ID (not name!) of your Google Cloud project, e.g. `my-phexpedition-prod-12345`
* `GOOGLE_KEY`: the JSON key of the `github` service account you previously created; just go to _IAM_ &rarr; _Service accounts_ &rarr; choose `github` &rarr; Keys &rarr; _Add key_ &rarr; _Create new key_ &rarr; _JSON_ and use the contents of the file just downloaded as the secret value
* `SONAR_KEY`: (!!! Only for environment `Test`): API Token from [Sonar Cloud](https://sonarcloud.io)

#### Initialize Google Cloud project

In Github, go to _Actions_. On the left pane, you should see _Initialize Google Cloud Project_ workflow.
Select it and click _Run workflow_ on the upper right (from main branch).

Since this workflow is typically only upon first setup, you have to manually
approve it using _Review deployments_ &rarr; [Environment].