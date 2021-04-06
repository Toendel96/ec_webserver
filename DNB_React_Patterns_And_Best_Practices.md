# Patterns and best practices

**Table of Contents**

- [Patterns and best practices](#patterns-and-best-practices)
    - [Components](#components)
    - [Component Properties](#component-properties)
    - [Components internal logic](#components-internal-logic)
    - [Conditional rendering](#conditional-rendering)
    - [Styling](#styling)
    - [Testing](#testing)
    - [Constants](#constants)
    - [Using Hooks](#using-hooks)
    - [Using Context](#using-context)
    - [Usage of Eufemia components](#usage-of-eufemia-components)
    - [HTTP requests](#http-requests)
    - [Lokalise text](#lokalise-text)
    - [Gatsby configurations](#gatsby-configurations)
    - [Routing and Navigation](#routing-and-navigation)

## Components

Use functional components instead of class components.

> Why? Functional components are easier to read and test, therefore easy to maintain and be understood across all the team.

```javascript
// Bad
class MyComponent extends React.Component {
  render() {
    return <p>{this.props.myProperty}</p>;
  }
}

// Good
function MyComponent({ myProperty }) {
  return <p>{myProperty}</p>;
}
```

### Component file strucuture

Each component should be declared in its own folder (inside the `components` folder), which contains the following
files:

- `MyComponent/MyComponent.js`: Contains the component itself (React).
- `MyComponent/MyComponent.test.js`: Contains the tests to the component (Jest).
- `MyComponent/MyComponent.module.scss`: Contains the styles declaration to the component (SASS).

Also, the component should be exported in the `components/index.js` file, to be easily accessible from any place of the
application

## Component Properties

The properties passed into the component should be native type properties (where possible).

> Why? Declaring the properties that a component accepts one-by-one makes the component usage easier. Also, it prevents child components from accidentally accessing/modifying properties in the object that aren't intended for them.

```javascript
// Bad
const values = { name: 'Name', description: 'Description' };
<MyComponent values={values} />

// Good
<MyComponent name="Name" description="Description" />
```

### Receiving properties

Always destruct the properties object, instead of using the props object.

> Why? It will make the component cleaner and easier to read.

```javascript
// Bad
function MyComponent(props) {
  return <p>{props.myProperty}</p>;
}

// Good
function MyComponent({ myProperty }) {
  return <p>{myProperty}</p>;
}
```

### Properties type checking

Always use PropTypes to validate the properties types.

```javascript
function MyComponent({ myProperty }) {
  return <p>{myProperty}</p>;
}

MyComponent.propTypes = {
  myProperty: PropTypes.string.isRequired,
};
```

More info [here](https://reactjs.org/docs/typechecking-with-proptypes.html).

## Components internal logic

Try to keep the component simple, move internal logic to functions outside the component whenever it's possible.

Avoid overflowing the component with auxiliary code that will make the component very difficult to read and understand.
Elegance is key 👌🏼

> Why? It's probably easier and more natural to add internal logic to the component every time it's necessary, but that will make the component less readable over time.

```javascript
// Bad
function MyComponent({ myProperty })) {

    let textToShow = '';
    switch(myProperty){
        case 'A':
            textToShow = 'Value is A';
            break;
        case 'B':
            textToShow = 'Oh no! The value is B';
            break;
        case 'C':
            textToShow = 'Do you C what is happening?';
            break;
        default:
            textToShow = 'Whatever';
    }

    return <p>{textToShow}</p>;
}

// Good
function MyComponent({ myProperty }) {
    return <p>{getTextToShow(myProperty)}</p>;
}

function getTextToShow(myProperty) {
    switch(myProperty){
        case 'A':
            return 'Value is A';
        case 'B':
            return 'Oh no! The value is B';
        case 'C':
            return 'Do you C what is happening?';
        default:
            return 'Whatever';
    }
}
```

## Conditional rendering

When the rendering of a component is conditional try to avoid _inline ifs with && operator_ and _ternary operators_.

> Why? _Inline ifs_ and _ternary operators_ are not bad and their usage is not discouraged, but sometimes inline conditional rendering can cause spaghetti code (especially when using nested conditions).

```javascript
// Bad
function MyComponent({ type, title, message })) {
    const [internalState, ...] = useState('Some Value');

    return <>
        { type === 'Error'
            ? <Icon type="error-message" />
            : type === 'Warning'
                ? <Icon type="warning-message" />
                : <Icon type="info-message" />
        }
        { title && <h1>{ title } {internalState}</h1>}
        { message }
    </>;
}

// Good
function MyComponent({ title, message }) {
    const [internalState, ...] = useState('Some Value');

    return <>
        { getIcon(type) }
        { getTitle() }
        { message }
    </>;

    // This function could be placed outside the components
    // but it's here to show that it's acceptable to have these
    // functions inside the component when using internal state
    function getTitle(){
        if(!title) return null;

        return <h1>{ title} {internalState}</h1>;
    }
}

function getIcon(type){
    switch(type){
        case 'Error':
            return <Icon type="error-message" />;
        case 'Warning':
            return <Icon type="warning-message" />;
        default:
            return <Icon type="info-message" />;
    }
}
```

## Styling

Use CSS modules instead of importing plain stylesheets or using styled-components.

```css
/* MyComponent.module.css */
.myComponentText {
  color: green;
}
```

```javascript
import styles from 'MyComponent.module.scss';

function MyComponent({ myProperty }) {
  return <p className={styles.myComponentText}>{myProperty}</p>;
}
```

### Naming convention

The class names should be written in camelCase.

- In CSS: declare the `.className`
- In JS: use `styles.className`

### Eufemia's CSS Custom Properties

When possible, use the Eufemia's CSS Custom Properties.

A list of all properties can be
found [here](https://eufemia.dnb.no/uilib/usage/customisation/styling#a-list-of-all-css-properties).

```css
/* Bad */
.myClass {
  padding: 1rem;
  color: #007272;
}

/* Good */
.myClass {
  padding: var(--spacing-small);
  color: var(--color-sea-green);
}
```

### Styles library

The `@pm-netbank/styles` library provides some styling features that you can use and add to the applications.

**Global styles**

In the application's default page (in the file `pages/index.js`), the global styles should be imported.

```javascript
import '@pm-netbank/styles/global.scss';
```

This will import some global styles that make all the applications have a similar look and feel (for example, the `body`
background-color is set in this file);

**Responsive breakpoints**

In some scenarios, it is necessary to write conditional styling based on the screen size.

In order to do that, should be used the same breakpoint values in all applications. Those breakpoint values are
available through Sass variables in the `breakpoints` file.

To use them, you need to import the file into your own `.scss` file.

```css
@import '@pm-netbank/styles/src/breakpoints.scss';

@media (min-width: $layout-small) {
  ...;
}
```

The available values are the ones provided
by [Eufemia](https://eufemia.dnb.no/uilib/usage/layout#media-queries-and-breakpoints).

## Testing

In order to have a sustainable and reliable codebase we should try to keep our testing coverage as high as possible.

We should follow a TDD (Test Driven Development) approach which will guarantee a better link between the tests and the
code and will also ensure good test coverage.

### How to test

We are using [Jest](https://jestjs.io/)
and [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/) to write and run the tests.

### Using data-testid

Using `data-testid` as an HTML attribute in the elements you want to test, allows you to access that element easily
using the test tools.

NB: All `data-testid` attributes will be removed when the apps are built.

```javascript
// MyComponent.js

...
<div data-testid="my-component-element">
    ...
</div>
...


// MyComponent.test.js

import { render } from '@testing-library/react';

describe('MyComponent', () => {
    it('renders the correct element', () => {
        const { queryByTestId } = render(<MyComponent />);

        expect(queryByTestId('my-component-element')).not.toBeNull();
    })
})

```

### Naming tests

Your `describe`, and `it` blocks should be human readable.

For example:

```javascript
describe('CardItem', () => {
  it('renders the correct element', () => {
    const { queryByTestId } = render(<MyComponent />);

    expect(queryByTestId('my-component-element')).not.toBeNull();
  });

  it('renders the correct button', () => {
    const { queryByTestId } = render(<MyComponent />);

    expect(queryByTestId('my-component-button')).not.toBeNull();
  });
});
```

The 2 tests above would would be read like this:

- CardItem renders the correct element
- CardItem renders the correct button

### Using the test-helpers library

There are some helpers created in the library `@pm-netbank/test-helpers`.

Check the documentation before you start creating tests.

If you have code that can be useful to other developers just add it to the library.

## Constants

If you're working with enums from the API, place them in a frozen object in `libs/pm-netbank/constants` if they are
global, or in your app's constants (`pm-netbank/<your-app>/constants`) if it's local, and import them as a regular
module.

Try to avoid hardcoded strings.

> Why? Using enums will make the code more readable, easier to change and less error prone to changes. Example: Today there is a `Card Status` named `"BLOCKED"`, if sometime in the future it is changed to `"BLOCK"` (or something else), we just have a change to make (the enum) and all the logic of the application will (probably) be the same.

```javascript
// Bad
    if(card.cardStatus === 'BLOCKED'){
        ...
    }

// Good
import { CardStatus } from '@pm-netbank/constants';

...

    if(card.cardStatus === CardStatus.BLOCKED){
        ...
    }

```

## Using Hooks

Under the `hooks` folder, you can place your custom hooks if you need to share state or behavior in different components
of your application.

> Why? Sometimes, we want to reuse some stateful logic between components.
>
> _From [React documentation](https://reactjs.org/docs/hooks-overview.html#building-your-own-hooks)_

P.S. hooks are free to create 😉

## Using Context

Under the `providers` folder, you can place your custom context providers if you need to create a handler to store some
data to be used in the Provider´s child components.

> Why? Context is designed to share data that can be considered “global” for a tree of React components.
> This is better than passing the same prop down the component tree unnecessarily.
>
> _From [React documentation](https://reactjs.org/docs/context.html#when-to-use-context)_

## Usage of Eufemia components

> **Why does this UI Library exist?**

> Simply, to unify and to maintain consistency of the most commonly used HTML Elements, custom components and patterns at DNB and to provide a platform for collaborative constant improvement. It's a part of the whole Design System.
> Eufemia components also ensure that we have all the accessibility boxes checked ✅
>
> _From [Eufemia documentation](https://eufemia.dnb.no/uilib/about-the-lib)_.

In summary, every time you need a component, just check first if it exists in Eufemia.

If you have questions or suggestions just use the Slack channel for
Eufemia [#eufemia-web](https://dnb-it.slack.com/archives/CMXABCHEY).

### HTML Elements

Beyond the custom components, Eufemia also provides components to replace the most commonly used HTML elements.

So, instead of using the plain HTML elements use the Eufemia's ones when possible.

That way, you will have out of the box the DNB styling and behavior applied.

```javascript
// Bad
    <div>
        <h1>Winter days</h1>
        <p>It's snowing outside</p>
    </div>

// Good
import { Div, H1, P } from 'dnb-ui-lib/elements';

...

    <Div>
        <H1>Winter days</H1>
        <P>It's snowing outside</P>
    </Div>

```

## HTTP requests

In the global libraries, there's an API handler (`@dnb/api-handler`) that encapsulates all the logic of executing HTTP
requests.

So, instead of using `axios`, `fetch`, or any other Javascript library, keep in mind the existence of this framework
library.

> Why? Instead of having multiple ways of performing HTTP requests across all the code base, this library will ensure all the requests are executed the same, with the right configuration and response structure.

Also, keep in mind that all the API addresses should be declared in the library `@pm-netbank/api-service`.

```javascript
// Bad
...

const response = await fetch('/cards/259865364');
const cardData = await response.json();

...

// Good
import { HttpMethods, useRequest } from '@dnb/api-handler';
import { Endpoints } from '@pm-netbank/api-service';

...

const cardDataRequest = useRequest(HttpMethods.GET, Endpoints().cardAction('259865364'));
cardDataRequest.execute();

```

## Lokalise text

The applications should be developed to work in a multi-language scenario (English and Norwegian Bokmål).

### Setting up the translations

The JSON files containing the translation texts should be placed under `src/services/i18n/translations/{en|no}`.

These files can be downloaded using the script `lokalise.sh` (you can find it at `pm-netbank/scripts`).

You can manage the translations in
the [Lokalise app](https://app.lokalise.com/project/374946525dee0caab01fb5.28319553/?view=multi).

### Using the translations

To be possible to use the translations the app should be wrapped within the `IntlProvider`.

```javascript
import { intlConfig } from '@pm-netbank/i18n';
import { IntlProvider } from 'react-intl';
import { translations } from '../services/i18n';

...

<IntlProvider {...intlConfig(translations, 'nb-NO')}>
    <AwesomeApp />
</IntlProvider>

```

Then, in the `IntlProvider` child components, it's possible to use specific components to render the translated texts.

Check out the [react-intl](https://formatjs.io/docs/react-intl) documentation to see what are the available components
and API functions to use localized text.

```javascript
// Component.js
import { FormattedMessage } from 'react-intl';

...

<FormattedMessage id="translation.key" />

...

```

## Gatsby configurations

There are two files that are always in the app, in order for `gatsby` to work properly: `gatsby-browser.js`
and `gatsby-ssr.js`.

These files contain the instructions for `gatsby` and must have a function called `wrapPageElement`, which allows a
plugin to wrap the page element.

Check [gatsby documentation](https://www.gatsbyjs.com/docs/reference/config-files/gatsby-browser/) for more information.

In order to keep the `gatsby` untouched and move all implementation bits to inside the `src` folder, should be created a
folder called `gatsby-wrappers` which will contain the wrappers to use in the `gatsby` function enumerated above.

With that, the files will look like this:

**`gastby-browser.js` and `gatsby-ssr.js`**

```javascript
import Layout from 'gatsby-dnb-layout';
import React from 'react';
import { PageElementWrapper } from './src/gatsby-wrappers';

/ eslint-disable-next-line dnb-application-rules/gatsby-browser-api
export function wrapPageElement({ element, props }) {
  element = <PageElementWrapper {...props}>{element}</PageElementWrapper>;

  /**
   * Only for local app dev –
   * We wrap the app in the default styling Layout component
   */
  if (!global.isRootApp) {
    return <Layout {...props}>{element}</Layout>;
  }

  return element;
}
```

**`gatsby-wrappers/index.js`**

```javascript
import { intlConfig } from '@pm-netbank/i18n';
import React from 'react';
import { IntlProvider } from 'react-intl';
import { ApiHandlerProvider } from '@dnb/api-handler';
import { DynamicRoutingProvider } from '@dnb/dynamic-routing';
import { createLocalErrorBoundary } from '@dnb/error-boundary';
import { Head } from '@dnb/layout';
import { translations } from '../services/i18n';

const [ErrorBoundary] = createLocalErrorBoundary();
/**
 * This is where you can declare all the providers your components need.
 *
 * In the PageElementWrapper you will receive all the dynamic path parameters
 * as described in the /pages folder structure, where any folder or file
 * called `[paramName].js` would result in `paramName` being passed to this
 * function as a dynamic parameter.
 *
 * NB: `paramName` is case sensitive.
 *
 * These parameters will then be passed to the routing provider and used to
 * determine which component to render.
 *
 * See the DynamicRoutingProvider for more information.
 *
 * This is being used in gatsby-browser.js and gatsby-ssr.js to render this
 * application.
 */
export function PageElementWrapper({ children, params }) {
  /* Due to the way gatsby assembles all the themes in the root application
   * we need to validate if the current requested URL is the one
   * corresponding to the current application.
   * Without this validation, gatsby would execute the wrapPageElement method
   * in gatsby-browser.js or gatsby-ssr.js for each one of the gatsby-themes
   * used in the root-app, leading to an invalid scenario of nested providers
   */

  if (!location.pathname.startsWith('/myApp')) {
    return children;
  }

  return (
    <DynamicRoutingProvider basePath="/myApp" parameters={params}>
      <IntlProvider {...intlConfig(translations, 'nb-NO')}>
        <ErrorBoundary>
          <ApiHandlerProvider appErrors={{}}>
            <AccountsDataProvider>
              <CardsDataProvider>{children}</CardsDataProvider>
            </AccountsDataProvider>
          </ApiHandlerProvider>
        </ErrorBoundary>
      </IntlProvider>
    </DynamicRoutingProvider>
  );
}
```

## Routing and Navigation

The routing structure is declared using a structure of folders and files, where each path represents a route and the
file contains the component that will be rendered in that path.

The route structure should be placed under `/pages` and the root components to be rendered in each one of the routes,
should be placed under `/routes`.

**Example: Show a payments list under the URL `dnb.no/payments`**

```javascript
// /pages/payments/index.js

export default function PaymentsListPage() {
  return <PaymentsListRoute />;
}
```

```javascript
// /routes/PaymentsListRoute.js

export default function PaymentsListRoute() {
  return (
    <>
      <H1>This is a Payments List</H1>
      <PaymentsListComponent />
    </>
  );
}
```

Follow the best practices for routing and navigation provided by the framework, available
at `dnb-web/docs/RoutingAndPages.md` and use the `@dnb/dynamic-routing` to handle with dynamic parameters and
navigation.

### Side panel Routes

This is a specific routing case that needs a slightly different approach.

To render these pages properly, it is necessary to render two routes: the background one and the side panel one.

**Example: In an application with a list of payments, show the details of one of the payments in a side panel (under the
URL `dnb.no/payments/76123GY62`)**

```javascript
// /pages/payments/[paymentId].js

export default function PaymentsDetailsPage() {
  return (
    <>
      <PaymentsListRoute />
      <PaymentsDetailsRoute />
    </>
  );
}
```

```javascript
// /routes/PaymentsDetailsRoute.js

export default function PaymentsDetailsRoute() {
  return (
    <>
      <H1>This is a single Payment</H1>
      <PaymentsDetailsComponent />
    </>
  );
}
```
