# UMLS Search Boot 🍃🥾

## Getting Started

To get started with UMLS Search Boot, follow the steps below:

1. Download MetaMapLite:
   - Visit the [NLM MetaMapLite](https://lhncbc.nlm.nih.gov/ii/tools/MetaMap/run-locally/MetaMapLite.html) page.
   - Download `2022AB UMLS Level 0+4+9 DataSet` zip file.
   - Extract the zip file.
   - Locate the `metamaplite-<version>-standalone.jar` at `target` directory.
   - Install MetaMapLite jar to local Maven repository.
        ```shell
        # windows powershell請把\換成`或移除\並寫成一行
        mvnw install:install-file \
            -Dfile=<path to metamaplite-version-standalone.jar> \
            -DgroupId=gov.nih.nlm.nls \
            -DartifactId=metamaplite \
            -Dversion=<version> \
            -Dpackaging=jar \
            -DgeneratePom=true
        ```
   - Configure MetaMapLite directory:
       - Locate the `src/main/resources/.env` file.
       - Set the `MMLITE_DIR` variable to the path that contains the `public_mm_lite` directory. For example, `/home/user/metamaplite`. 
   
2. Set up Environment Variables:
   - Locate the `src/main/resources/.env` file.
   - Review the provided `.env.example` file for detailed instructions on setting up the required environment variables.
   - Make sure to configure the environment variables correctly based on your specific setup.

3. Run the Application:
   - Open a terminal or command prompt.
   - Navigate to the project root directory.
   - Execute the following command:
     ```sh
     mvn spring-boot:run
     ```
   - This command will start the application listening on http://localhost:8080 , your can change the port at .env file or application.properties.

## Testing

To test UMLS Search Boot, you have two options:

1. Using IntelliJ IDEA:
   - Open IntelliJ IDEA.
   - Navigate to the `src/test/java` directory.
   - Right-click on the directory and select "Run 'All Tests'".
   - IntelliJ IDEA will execute all the tests and display the results.

2. Using Maven:
   - Open a terminal or command prompt.
   - Navigate to the project root directory.
   - Execute the following command:
     ```sh
     mvn test
     ```
   - Maven will run all the tests in the project and provide the test results.

## API

### Search Concept by CUI

Search for a concept using the Concept Unique Identifier (CUI).

- Endpoint: `/api/v1/concepts/{cui}`
- Method: GET
- Parameters: `cui` (required)
- Example: http://localhost:8080/api/v1/concepts/C5397597

#### Response:
- Status Code: 200 (OK)
- Response Format: JSON
- Example Response:
<details>
<summary>API Response Example (click to expand)</summary>

```json
{
  "cui": "C0948008",
  "preferredName": "Ischemic stroke",
  "definitions": [
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "meaning": "<p>A <a href=\"https://medlineplus.gov/stroke.html\">stroke</a> is a medical emergency. There are two types - ischemic and <a href=\"https://medlineplus.gov/hemorrhagicstroke.html\">hemorrhagic</a>. Ischemic stroke is the more common type. It is usually caused by a <a href=\"https://medlineplus.gov/bloodclots.html\">blood clot</a> that blocks or plugs a blood vessel in the brain. This keeps blood from flowing to the brain. Within minutes, brain cells begin to die. Another cause is stenosis, or narrowing of the artery. This can happen because of <a href=\"https://medlineplus.gov/atherosclerosis.html\">atherosclerosis</a>, a disease in which plaque builds up inside your arteries. <a href=\"https://medlineplus.gov/transientischemicattack.html\">Transient ischemic attacks</a> (TIAs) occur when the blood supply to the brain is interrupted briefly. Having a TIA can mean you are at risk for having a more serious stroke.</p> <p>Symptoms of stroke are</p> <ul> <li>Sudden numbness or weakness of the face, arm or leg (especially on one side of the body)</li> <li>Sudden confusion, trouble speaking or understanding speech</li> <li>Sudden trouble seeing in one or both eyes</li> <li>Sudden trouble walking, dizziness, loss of balance or coordination</li> <li>Sudden severe headache with no known cause</li> </ul> <p>It is important to treat strokes as quickly as possible. <a href=\"https://medlineplus.gov/bloodthinners.html\">Blood thinners</a> may be used to stop a stroke while it is happening by quickly dissolving the blood clot. <a href=\"https://medlineplus.gov/strokerehabilitation.html\">Post-stroke rehabilitation</a> can help people overcome disabilities caused by stroke damage.</p> <p class=\"\">NIH: National Institute of Neurological Disorders and Stroke</p>",
      "sourceName": "MEDLINEPLUS"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "meaning": "An acute episode of focal cerebral, spinal, or retinal dysfunction caused by infarction of brain tissue.",
      "sourceName": "NCI"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "meaning": "Stroke due to BRAIN ISCHEMIA resulting in interruption or reduction of blood flow to a part of the brain. When obstruction is due to a BLOOD CLOT formed within in a cerebral blood vessel it is a thrombotic stroke. When obstruction is formed elsewhere and moved to block a cerebral blood vessel (see CEREBRAL EMBOLISM) it is referred to as embolic stroke. Wake-up stroke refers to ischemic stroke occurring during sleep while cryptogenic stroke refers to ischemic stroke of unknown origin.",
      "sourceName": "MSH"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "meaning": "Acute ischemic stroke (AIS) is defined by the sudden loss of blood flow to an area of the brain with the resulting loss of neurologic function. It is caused by thrombosis or embolism that occludes a cerebral vessel supplying a specific area of the brain. During a vessel occlusion, there is a core area where damage to the brain is irreversible and an area of penumbra where the brain has lost function owing to decreased blood flow but is not irreversibly injured. [PMID:32054610]",
      "sourceName": "HPO"
    }
  ],
  "synonyms": [
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischaemic Stroke",
      "sourceName": "MSH"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "STROKE, ISCHEMIC",
      "sourceName": "OMIM"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Stroke, ischemic",
      "sourceName": "OMIM"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischemic Strokes",
      "sourceName": "MSH"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "ischaemic strokes",
      "sourceName": "CHV"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischaemic stroke",
      "sourceName": "HPO"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischemic stroke",
      "sourceName": "MTH"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "ischemic stroke",
      "sourceName": "CHV"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "ischemic strokes",
      "sourceName": "CHV"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "stroke ischemic",
      "sourceName": "CHV"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischemic stroke",
      "sourceName": "HPO"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Stroke, Ischemic",
      "sourceName": "MSH"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischemic Stroke",
      "sourceName": "MSH"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischemic stroke",
      "sourceName": "OMIM"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischemic Stroke",
      "sourceName": "NCI"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Stroke, Ischaemic",
      "sourceName": "MSH"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischemic Stroke",
      "sourceName": "MEDLINEPLUS"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "ischaemic stroke",
      "sourceName": "CHV"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischaemic Strokes",
      "sourceName": "MSH"
    },
    {
      "concept": {
        "cui": "C0948008",
        "preferredName": "Ischemic stroke"
      },
      "term": "Ischemic Cerebrovascular Accident",
      "sourceName": "NCI"
    }
  ],
  "semanticTypes": [
    "Disease or Syndrome"
  ],
  "broaderConcepts": [],
  "narrowerConcepts": [
    "C5392097",
    "C5392832",
    "C5392833"
  ]
}
```
</details>

#### Error Responses:
- Status Code: 404 (Not Found)
- Response Format: text/plain
- Example Response:
    ```text
    Could not found concept cui: <cui>
    ```

### Search Concepts by Freetext

Search for concepts using the freetext.


- Endpoint: `/api/v1/concepts`
- Method: GET
- Parameters

| Parameter   | Type   | Required | Description              |
|-------------|--------|----------|--------------------------|
| queryText   | string | Yes      | The freetext query string |

- Example: http://localhost:8080/api/v1/concepts?queryText=root
#### Response:

- Status Code: 200 (OK)
- Response Format: JSON
- Example Response: "array of cui (search for concept by cui example)"

#### Error Responses:
- Status Code: 500 (Internal Server Error)
- Response Format: text/plain
- Example Response:
    ```text
    Error while processing freetext to cuis
    ```
