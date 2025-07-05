*** Settings ***
Library    SeleniumLibrary

*** Variables ***
${remote_url}=    http://localhost:4444/wd/hub

*** Keywords ***
Open Session
    [Arguments]    ${capabilities}    ${test_url}
    open browser    remote_url=${remote_url}    browser=chrome    desired_capabilities=${capabilities}   url=${test_url}

Close Session
    close browser

Add Implicit Wait
    set selenium implicit wait    5

Get the page title
    get title

Verify Local Page
    Title Should be     BrowserStack Local

Add first product to cart
    click element    xpath=//*[@id="1"]/div[4]

Verify product is added to cart
    ${product_name}    get text    xpath=//*[@id="1"]/p
    ${product_incart}    get text    css=p.title 
    element should contain    css=p.title    ${product_name}
