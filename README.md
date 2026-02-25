# ccf-core

`ccf-core` (Container Component Framework Core) is a database-driven response composition engine.

It lets you define **sections**, **containers**, **fields**, and **queries** in DB tables, then generate structured UI/data payloads at runtime through one API:

```java
ContainerComponentResponse execute(ContainerComponentRequest request);
```

The framework is useful when you want to ship configurable screens or API payloads without hardcoding every query and response shape in Java.

## What It Solves

- Builds containerized response payloads from DB metadata + SQL.
- Supports multiple container styles (`DATA_TABLE`, `RAW_DATA_TABLE`, `MULTI_INFO`, etc.).
- Adds dynamic filtering, sorting, pagination through query placeholders.
- Supports hook points (bean + method) at section/container/row/field levels.
- Allows physical table-name remapping through YAML config.

## Maven

```xml
<dependency>
  <groupId>com.github.salilvnair</groupId>
  <artifactId>ccf-core</artifactId>
  <version>1.0.2</version>
</dependency>
```

## Enable In Spring Boot

```java
import com.github.salilvnair.ccf.annotation.EnableCcfCore;

@EnableCcfCore
@SpringBootApplication
public class App {
}
```

`@EnableCcfCore` imports `CcfCoreAutoConfiguration`, which wires entity scan, repositories, and component scan for `ccf-core` packages.

## Consumer Configuration (Dynamic Table Mapping)

`ccf-core` entity classes use default table names like `CCF_SECTION_INFO`, `CCF_CONTAINER_INFO`, etc.  
You can remap them to your own physical tables via `ccf.core.tables`.

```yaml
ccf:
  core:
    tables:
      PAGE_COMMON_QUERY: ZP_PAGE_COMMON_QUERY
      SECTION_INFO: ZP_SECTION_INFO
      CONTAINER_INFO: ZP_CONTAINER_INFO
      CONTAINER_FIELD_INFO: ZP_CONTAINER_FIELD_INFO
      CONTAINER_QUERY_INFO: ZP_CONTAINER_QUERY_INFO
```

How it works:
- `CcfCorePhysicalNamingStrategy` intercepts names starting with `CCF_`.
- It strips the prefix and uses the YAML key (`SECTION_INFO`, `CONTAINER_INFO`, etc.).
- If a key exists, Hibernate uses your mapped table name.

## Primary Service Contract

```java
public interface CcfCoreService {
    ContainerComponentResponse execute(ContainerComponentRequest request);
}
```

### Request

`ContainerComponentRequest`:
- `requestTypes`: list of `CONTAINER` and/or `COMPONENT`
- `pageInfo`: list of `PageInfoRequest` (required for container flow)
- `componentDataRequest`: optional (for component flow)

`PageInfoRequest` (important fields):
- identity/scope: `pageId`, `sectionId`, `containerId`, `sectionIds`, `containerIds`, `productIds`
- user context: `userId`, `loggedInUserId`
- inputs: `inputParams`
- controls: `filter`, `sort`, `paginate`, `loadFilterValues`
- query modifiers: `filterParams`, `sortParams`, `containerFilterParams`, `containerSortParams`, `filterFieldId`
- pagination: `paginationInfo`, `containerPaginationInfo`

### Response

`ContainerComponentResponse`:
- `pages` -> map keyed by `pageId`
- optional `componentDataResponse`

Each page contains:
- sections (`Map<sectionId, SectionData>`)

Each section contains a list of `ContainerData` objects:
- info-style output: `data`
- table output: `tableHeaders`, `tableData`
- raw table output: `rawTableData`
- optional: `paginationInfo`, `filterValues`, `dtFilterValues`, `metaData`

## DB Model (Core Metadata Tables)

### 1) `SECTION_INFO`
Defines active sections for a page and optional section-level task hook.

Key columns:
- `page_id`, `section_id`, `active`
- `bean_name`, `bean_method_name` (optional section task)
- `product` (optional product scoping)

Example:

| page_id | section_id | active | bean_name | bean_method_name |
|---|---:|---|---|---|
| 1 | 1 | Y | faqSearchDataTask | injectFaqQueryEmbedding |

### 2) `PAGE_COMMON_QUERY`
Stores page-scoped shared queries (reusable context/common data).

Key columns:
- `page_common_query_id`, `page_id`
- `query_string`, `query_params`

### 3) `CONTAINER_INFO`
Defines containers under a section.

Key columns:
- identity/scope: `container_id`, `section_id`, `active`, `load_by_default`
- type/display: `container_type`, `container_display_name`
- hooks: `bean_name`, `bean_method_name`
- row hooks (table): `tbl_row_bean_name`, `tbl_row_bean_method_name`
- hierarchy: `parent_container_id`
- authorization/scope: `mapped_roles`, `product`

Example:

| container_id | section_id | container_type | container_display_name |
|---:|---:|---|---|
| 101 | 1 | DATA_TABLE | FAQ Search Results |
| 3 | 1 | RAW_DATA_TABLE | Raw FAQ Search Results |

### 4) `CONTAINER_QUERY_INFO`
SQL per container.

Key columns:
- `container_query_id`, `container_id`
- `query_string`
- `count_query_string`
- `pagination_query_string`
- `query_params` (comma-separated parameter keys)
Example:

| container_query_id | container_id | query_string | query_params  |
|---:|---:|---|---|
| 5001 | 101 | SELECT faq_id, question, answer, 1 - (embedding <=> CAST(:faqQueryEmbedding AS vector)) AS score FROM zp_faq WHERE enabled = true ORDER BY embedding <=> CAST(:faqQueryEmbedding AS vector) LIMIT 10 | faqQueryEmbedding |
| 3 | 3 | select * FROM zp_faq WHERE enabled = true |  |

### 5) `CONTAINER_FIELD_INFO`
Describes output fields for a container.

Key columns:
- identity: `container_field_id`, `container_id`, `group_id`
- mapping: `field_display_name`, `mapped_column_name`, `display_order`
- behavior: `filterable`, `sortable`, `editable`, `required`, `visible`, `enabled`
- type/validation: `field_type`, `field_type_id`, `field_length`, `field_min_length`, `field_max_length`, `field_format`
- hooks: `bean_name`, `bean_method_name`
- nested container support: `sub_container_id`

## Supported Container Types

`ContainerType` values in code:
- `MULTI_ROW`
- `FILTER_VALUE_MULTI_ROW`
- `MULTI_INFO`
- `DATA_TABLE`
- `RAW_DATA_TABLE`
- `SIMPLE_DATA_TABLE`
- `INFO_TABLE`
- `INFO`

Practical behavior:
- `INFO`: single-row/single-record container-style output.
- `MULTI_INFO`: multiple fields/records style info output.
- `DATA_TABLE`: structured table output with headers + row field objects.
- `RAW_DATA_TABLE`: list of raw maps (`rawTableData`) from SQL.
- `MULTI_ROW` / `FILTER_VALUE_MULTI_ROW`: specialized multi-value field containers.

## Query Placeholder & Runtime Decoration

The engine decorates SQL at runtime using placeholder tokens (for filter/sort/pagination logic):
- `[WHERE_FILTER_BY_PLACE_HOLDER]`
- `[ORDER_BY_PLACE_HOLDER]`
- `[ROW_NUMBER_ORDER_BY_PLACE_HOLDER]`
- `[MULTI_CONDITION_WHERE_FILTER_BY_PLACE_HOLDER]`
- `[FILTER_BY_FIELD_PLACEHOLDER]`
- `[FETCH_FIRST_N_ROWS_PLACE_HOLDER]`
- `[PICK_FROM_COMMON_QUERY]`

This enables one metadata query template to support many runtime combinations.

## Execution Flow (Container Request)

1. `execute()` checks request types.
2. For each `PageInfoRequest`:
   - Load active sections (`SECTION_INFO`) for page/section scope.
   - Build `DataContext` from user/page/input/filter/sort/pagination settings.
3. Per section:
   - Load `PAGE_COMMON_QUERY` entries into context.
   - Run section generator (`COMMON` by default).
   - Optional section bean task executes (`section_info.bean_name/method`).
4. Per container:
   - Resolve container by section + active/load defaults.
   - Resolve container query (`CONTAINER_QUERY_INFO`).
   - Resolve container fields (`CONTAINER_FIELD_INFO`).
   - Run generator based on `container_type`.
   - Apply pagination/filter/sort.
   - Execute container/row/field hooks when configured.
5. Return composed `ContainerComponentResponse`.

## Hooking Custom Logic (Data Tasks)

`DataTaskExecutor` can invoke Spring beans + methods declared in DB metadata.

Hook points:
- Section-level: `SECTION_INFO.bean_name` + `bean_method_name`
- Container-level: `CONTAINER_INFO.bean_name` + `bean_method_name`
- Table row-level: `CONTAINER_INFO.tbl_row_bean_name` + `tbl_row_bean_method_name`
- Field-level: `CONTAINER_FIELD_INFO.bean_name` + `bean_method_name`

You can use this to:
- inject derived input params (e.g., vector embedding)
- post-process row values
- enrich display data
- transform fields conditionally

## FAQ Search Example (Using Your Sample)

### Metadata setup summary

- `SECTION_INFO` defines page/section and invokes `faqSearchDataTask.injectFaqQueryEmbedding`.
- `CONTAINER_INFO` has:
  - `101 -> DATA_TABLE (FAQ Search Results)`
  - `3 -> RAW_DATA_TABLE (Raw FAQ Search Results)`
- `CONTAINER_QUERY_INFO`:
  - container `101` has embedding similarity query
  - container `3` has raw SQL `select * FROM zp_faq WHERE enabled = true`
- `CONTAINER_FIELD_INFO` maps output columns like `faq_id`, `question`, `answer`, `category`, `tags`.

### Consumer-side invocation

```java
@Autowired
private CcfCoreService ccfCoreService;

public ContainerComponentResponse searchFaq(String userId, String queryText) {
    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("query", queryText);

    PageInfoRequest pageInfo = PageInfoRequest.builder()
            .userId(userId)
            .loggedInUserId(userId)
            .pageId(1)
            .sectionId(1)
            .inputParams(inputParams)
            .paginate(false)
            .filter(false)
            .sort(false)
            .build();

    ContainerComponentRequest request = new ContainerComponentRequest();
    request.setRequestTypes(List.of(RequestType.CONTAINER));
    request.setPageInfo(List.of(pageInfo));

    return ccfCoreService.execute(request);
}
```

### Runtime behavior

- Section task computes embedding from user query and injects `faqQueryEmbedding` into input params.
- `DATA_TABLE` container query uses `:faqQueryEmbedding` and returns ranked results.
- `RAW_DATA_TABLE` container returns raw DB rows for diagnostics/debug/raw UI rendering.
- Final payload contains both containers under section `1`.

## Optional HTTP Adapter

A ready controller exists in library:
- `POST /v1/generate`
- body: `ContainerComponentRequest`
- returns: `ContainerComponentResponse`

Use directly or wrap with your own API boundary.

## Best Practices

- Keep SQL in `CONTAINER_QUERY_INFO` focused and deterministic.
- Put only reusable values in `PAGE_COMMON_QUERY`.
- Use `load_by_default='Y'` for default UX containers.
- Use container/field hooks only for enrichment, not large business workflows.
- For table containers, always maintain `display_order` in `CONTAINER_FIELD_INFO`.
- Keep `query_params` aligned with keys present in `inputParams`.

## Troubleshooting

- Empty response:
  - Check `active='Y'` across section/container/field rows.
  - Verify page/section/container ids in request.
- SQL params missing:
  - Ensure parameter names are listed in `query_params` and present in `inputParams`.
- Filters/sort not applied:
  - Ensure `filter=true` / `sort=true` and placeholder tokens exist in SQL template.
- Wrong table names:
  - Verify `ccf.core.tables.*` keys match expected names (`SECTION_INFO`, `CONTAINER_INFO`, etc.).
- Hook not executing:
  - Validate bean exists in Spring context and method signature matches `DataTaskContext`.

## Quick Recap

`ccf-core` gives you a metadata-first model:
- DB tables define what to fetch and how to shape it.
- Java service executes one request and returns page/section/container structured response.
- You can evolve behavior mostly through data + task hooks, with minimal code changes.

## Sample Request/Response JSON

### Sample Request (`POST /v1/generate`)

```json
{
  "requestTypes": ["CONTAINER"],
  "pageInfo": [
    {
      "userId": "u-1001",
      "loggedInUserId": "u-1001",
      "pageId": 1,
      "sectionId": 1,
      "inputParams": {
        "query": "How do I reset my password?",
        "faqQueryEmbedding": "[0.0123, -0.9944, 0.2211, ...]"
      },
      "filter": false,
      "sort": false,
      "paginate": false
    }
  ]
}
```

### Sample Response

```json
{
  "pages": {
    "1": {
      "pageId": 1,
      "sections": {
        "1": {
          "data": [
            {
              "containerId": 101,
              "sectionId": 1,
              "containerDisplayName": "FAQ Search Results",
              "containerType": "DATA_TABLE",
              "tableHeaders": [
                { "fieldId": 1001, "fieldDisplayName": "FAQ ID", "fieldType": "TEXT" },
                { "fieldId": 1002, "fieldDisplayName": "Question", "fieldType": "TEXT" },
                { "fieldId": 1003, "fieldDisplayName": "Answer", "fieldType": "TEXT" },
                { "fieldId": 1004, "fieldDisplayName": "Category", "fieldType": "TEXT" },
                { "fieldId": 1005, "fieldDisplayName": "Tags", "fieldType": "TEXT" }
              ],
              "tableData": [
                [
                  { "fieldId": 1001, "fieldValue": 501 },
                  { "fieldId": 1002, "fieldValue": "How can I reset my password?" },
                  { "fieldId": 1003, "fieldValue": "Use Forgot Password on login." },
                  { "fieldId": 1004, "fieldValue": "Account" },
                  { "fieldId": 1005, "fieldValue": "password,login,security" }
                ]
              ]
            },
            {
              "containerId": 3,
              "sectionId": 1,
              "containerDisplayName": "Raw FAQ Search Results",
              "containerType": "RAW_DATA_TABLE",
              "rawTableData": [
                {
                  "FAQ_ID": 501,
                  "QUESTION": "How can I reset my password?",
                  "ANSWER": "Use Forgot Password on login.",
                  "CATEGORY": "Account",
                  "TAGS": "password,login,security"
                }
              ]
            }
          ],
          "metaData": {
            "sectionId": 1
          }
        }
      }
    }
  }
}
```

Notes:
- `tableHeaders` and `tableData` are produced for `DATA_TABLE`-style containers.
- `rawTableData` is produced for `RAW_DATA_TABLE` containers.
- Actual field sets depend on rows in `CONTAINER_FIELD_INFO` and SQL in `CONTAINER_QUERY_INFO`.
