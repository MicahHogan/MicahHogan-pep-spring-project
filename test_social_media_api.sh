#!/bin/bash

BASE_URL="http://localhost:8080"

# # Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# Function to print a section header
print_section() {
  echo -e "\n${YELLOW}========================================"
  echo -e "  ${RED}$1"
  echo -e "${YELLOW}========================================${NC}"
}

# Function to print a section footer
print_footer() {
    echo -e "${YELLOW}========================================${NC}"
    echo -e "${YELLOW}****************************************\n"
}
# Function to handle checking of valid JSON response
check_valid_json() {
  if echo "$1" | jq . >/dev/null 2>&1; then
    echo -e "${GREEN}Valid JSON response received.${NC}"
  else
    echo -e "${RED}Invalid JSON response!${NC}"
    exit 1
  fi
}

# Test Home endpoint
print_section "Testing: Home endpoint"
home_response=$(curl -s -X GET $BASE_URL/)
echo -e "Raw Home Response: $home_response"
check_valid_json "$home_response"
echo "Home Response: $(echo $home_response | jq .message)"
print_footer

# Test Register a new user
print_section "Testing: Register a new user"
register_response=$(curl -s -X POST $BASE_URL/register -H "Content-Type: application/json" -d '{"username":"testuser5","password":"password123"}')
echo -e "Raw Register Response: $register_response"
check_valid_json "$register_response"
account_id=$(echo "$register_response" | jq -r '.accountId')
echo "Successfully registered user with accountId: $account_id"
print_footer

# Test Login
print_section "Testing: Log in a user"
login_response=$(curl -s -X POST $BASE_URL/login -H "Content-Type: application/json" -d '{"username":"testuser5","password":"password123"}')
echo -e "Raw Login Response: $login_response"
check_valid_json "$login_response"
echo "Login successful. Account: $(echo $login_response | jq .username)"
print_footer

# Test Get All Messages
print_section "Testing: Get all messages"
messages_response=$(curl -s -X GET $BASE_URL/messages)
echo -e "Raw Messages Response: $messages_response"
check_valid_json "$messages_response"
echo "Number of messages: $(echo $messages_response | jq length)"
print_footer

# Test Create a new message
print_section "Testing: Create a new message"
current_timestamp=$(date +%s000)
create_message_response=$(curl -s -X POST $BASE_URL/messages \
  -H "Content-Type: application/json" \
  -d "{\"messageText\":\"This is a test message\", \"postedBy\":$account_id, \"timePostedEpoch\":$current_timestamp}")

echo -e "Raw Create Message Response: $create_message_response"
check_valid_json "$create_message_response"

# Extract the real message ID from the response
message_id=$(echo "$create_message_response" | jq -r '.messageId')

if [ "$message_id" != "null" ]; then
    echo "Successfully created message with messageId: $message_id"
else
    echo "Failed to create message. Response: $create_message_response"
    exit 1
fi  

# Test Get Message by ID
print_section "Testing: Get message by ID"
get_message_response=$(curl -s -X GET $BASE_URL/messages/$message_id)
echo -e "Raw Get Message Response: $get_message_response"
check_valid_json "$get_message_response"
echo "Retrieved message: $(echo $get_message_response | jq .messageText)"
print_footer

# Test Update Message
print_section "Testing: Update message"
update_message_response=$(curl -s -X PATCH $BASE_URL/messages/$message_id -H "Content-Type: application/json" -d '{"messageText":"Updated message text"}')
echo -e "\nRaw Update Message Response: $update_message_response"
check_valid_json "$update_message_response"
echo "Updated message, rows affected: $update_message_response"
print_footer

# Verify the update by getting the message again
print_section "Testing: Verify message update"
updated_message=$(curl -s -X GET $BASE_URL/messages/$message_id)
echo -e "\nUpdated message content: $(echo $updated_message | jq .messageText)"
print_footer

# Test Get All Accounts
print_section "Testing: Get all accounts"
accounts_response=$(curl -s -X GET $BASE_URL/accounts)
echo -e "\nRaw Accounts Response: $accounts_response"
check_valid_json "$accounts_response"
echo "Number of accounts: $(echo $accounts_response | jq length)"
print_footer

# Test Get Messages by User
print_section "Testing: Get messages by user"
messages_by_user_response=$(curl -s -X GET $BASE_URL/accounts/$account_id/messages)
echo -e "\nRaw Messages by User Response: $messages_by_user_response"
check_valid_json "$messages_by_user_response"
echo "Number of messages by user $account_id: $(echo $messages_by_user_response | jq length)"
print_footer

# First delete the messages associated with the user
print_section "Testing: Delete message by ID"
delete_message_response=$(curl -s -X DELETE $BASE_URL/messages/$message_id)
echo -e "\nRaw Delete Message Response: $delete_message_response"
check_valid_json "$delete_message_response"
echo "Deleted message with ID: $message_id, rows affected: $delete_message_response"
print_footer

# Then delete the user
print_section "Testing: Delete user by accountId"
delete_user_response=$(curl -s -X DELETE $BASE_URL/accounts/$account_id)
echo -e "\nRaw Delete User Response: $delete_user_response"
check_valid_json "$delete_user_response"
echo "Result of deleting user with accountId $account_id: $(echo $delete_user_response | jq .message)"
print_footer


# #!/bin/bash

# # =============================================
# # Configuration and Parameters
# # =============================================
# BASE_URL="${API_URL:-http://localhost:8080}"
# TEST_USERNAME="${TEST_USERNAME:-testuser_$(date +%s)}"
# TEST_PASSWORD="${TEST_PASSWORD:-password123}"
# TEST_MESSAGE="${TEST_MESSAGE:-This is a test message created on $(date)}"
# TEST_UPDATED_MESSAGE="${TEST_UPDATED_MESSAGE:-This message was updated on $(date)}"

# # Error test parameters
# INVALID_USERNAME="${INVALID_USERNAME:-}" # Empty username
# INVALID_PASSWORD="${INVALID_PASSWORD:-}" # Empty password
# INVALID_MESSAGE="${INVALID_MESSAGE:-}" # Empty message
# INVALID_ID=999999 # Non-existent ID

# # Performance thresholds (in seconds)
# SLOW_THRESHOLD=1.0

# # Test result counters
# TESTS_TOTAL=0
# TESTS_PASSED=0
# TESTS_FAILED=0

# # Colors for output
# GREEN='\033[0;32m'
# RED='\033[0;31m'
# YELLOW='\033[0;33m'
# NC='\033[0m' # No Color

# # =============================================
# # Utility Functions
# # =============================================

# # Function to check valid JSON response
# check_valid_json() {
#   if echo "$1" | jq . >/dev/null 2>&1; then
#     return 0
#   else
#     return 1
#   fi
# }

# # Function to run and time a test
# run_test() {
#   local test_name="$1"
#   local command="$2"
#   local expected_status="${3:-200}"
  
#   TESTS_TOTAL=$((TESTS_TOTAL+1))
  
#   echo -e "\n${YELLOW}[$TESTS_TOTAL] Testing: $test_name${NC}"
  
#   # Measure start time
#   local start_time=$(date +%s.%N)
  
#   # Run the command and capture response and status code
#   local response=$(eval "$command")
#   local status=$?
  
#   # Measure end time and calculate duration
#   local end_time=$(date +%s.%N)
#   local duration=$(echo "$end_time - $start_time" | bc)
  
#   # Check if JSON is valid
#   local json_valid=true
#   check_valid_json "$response" || json_valid=false
  
#   # Display results
#   echo -e "Response: $response"
  
#   # Check test result
#   if [[ "$json_valid" == true ]]; then
#     echo -e "${GREEN}✓ Valid JSON response${NC}"
#     TESTS_PASSED=$((TESTS_PASSED+1))
#   else
#     echo -e "${RED}✗ Invalid JSON response${NC}"
#     TESTS_FAILED=$((TESTS_FAILED+1))
#     return
#   fi
  
#   # Performance check
#   if (( $(echo "$duration > $SLOW_THRESHOLD" | bc -l) )); then
#     echo -e "${YELLOW}⚠ Slow response time: ${duration}s (threshold: ${SLOW_THRESHOLD}s)${NC}"
#   else
#     echo -e "${GREEN}✓ Response time: ${duration}s${NC}"
#   fi
  
#   # Return the response for further processing
#   echo "$response"
# }

# # Function to print a section header
# print_section() {
#   echo -e "\n${YELLOW}========================================"
#   echo -e "  $1"
#   echo -e "========================================${NC}\n"
# }

# # Function to print test summary
# print_summary() {
#   echo -e "\n${YELLOW}========================================"
#   echo -e "  TEST SUMMARY"
#   echo -e "========================================${NC}"
#   echo -e "Total tests:  $TESTS_TOTAL"
#   echo -e "${GREEN}Tests passed: $TESTS_PASSED${NC}"
#   echo -e "${RED}Tests failed: $TESTS_FAILED${NC}"
  
#   if [ $TESTS_FAILED -eq 0 ]; then
#     echo -e "\n${GREEN}✓ All tests passed!${NC}"
#   else
#     echo -e "\n${RED}✗ Some tests failed!${NC}"
#   fi
# }

# # =============================================
# # Main Test Flow
# # =============================================

# # Print test configuration
# print_section "TEST CONFIGURATION"
# echo "Base URL: $BASE_URL"
# echo "Test Username: $TEST_USERNAME"
# echo "Test Password: $TEST_PASSWORD"
# echo "Test Message: $TEST_MESSAGE"

# # =============================================
# # 1. Test Home Endpoint
# # =============================================
# print_section "TESTING HOME ENDPOINT"

# home_response=$(run_test "Home endpoint" "curl -s -X GET $BASE_URL/")

# # =============================================
# # 2. Test User Registration (Success case)
# # =============================================
# print_section "TESTING USER REGISTRATION"

# register_response=$(run_test "Register a new user" "curl -s -X POST $BASE_URL/register -H \"Content-Type: application/json\" -d '{\"username\":\"$TEST_USERNAME\",\"password\":\"$TEST_PASSWORD\"}'")
# account_id=$(echo "$register_response" | jq -r '.accountId')

# if [ "$account_id" != "null" ] && [ -n "$account_id" ]; then
#   echo -e "${GREEN}✓ Successfully registered user with accountId: $account_id${NC}"
# else
#   echo -e "${RED}✗ Failed to extract accountId from response${NC}"
#   TESTS_FAILED=$((TESTS_FAILED+1))
# fi

# # =============================================
# # 3. Test Registration with Invalid Data
# # =============================================
# print_section "TESTING INVALID REGISTRATION"

# invalid_register_response=$(run_test "Register with invalid username" "curl -s -X POST $BASE_URL/register -H \"Content-Type: application/json\" -d '{\"username\":\"$INVALID_USERNAME\",\"password\":\"$TEST_PASSWORD\"}'")

# invalid_register_response=$(run_test "Register with invalid password" "curl -s -X POST $BASE_URL/register -H \"Content-Type: application/json\" -d '{\"username\":\"$TEST_USERNAME\",\"password\":\"$INVALID_PASSWORD\"}'")

# # =============================================
# # 4. Test Login (Success case)
# # =============================================
# print_section "TESTING LOGIN"

# login_response=$(run_test "Login with valid credentials" "curl -s -X POST $BASE_URL/login -H \"Content-Type: application/json\" -d '{\"username\":\"$TEST_USERNAME\",\"password\":\"$TEST_PASSWORD\"}'")
# logged_in_username=$(echo "$login_response" | jq -r '.username')

# if [ "$logged_in_username" == "$TEST_USERNAME" ]; then
#   echo -e "${GREEN}✓ Successfully logged in as: $logged_in_username${NC}"
# else
#   echo -e "${RED}✗ Login username mismatch or login failed${NC}"
#   TESTS_FAILED=$((TESTS_FAILED+1))
# fi

# # =============================================
# # 5. Test Login with Invalid Credentials
# # =============================================
# print_section "TESTING INVALID LOGIN"

# invalid_login_response=$(run_test "Login with invalid credentials" "curl -s -X POST $BASE_URL/login -H \"Content-Type: application/json\" -d '{\"username\":\"$TEST_USERNAME\",\"password\":\"wrong_password\"}'")

# # =============================================
# # 6. Test Get All Messages
# # =============================================
# print_section "TESTING MESSAGE RETRIEVAL"

# messages_response=$(run_test "Get all messages" "curl -s -X GET $BASE_URL/messages")
# message_count=$(echo "$messages_response" | jq length)
# echo -e "Found $message_count existing messages"

# # =============================================
# # 7. Test Message Creation
# # =============================================
# print_section "TESTING MESSAGE CREATION"

# # Generate current timestamp in milliseconds
# current_timestamp=$(date +%s000)

# create_message_response=$(run_test "Create a new message" "curl -s -X POST $BASE_URL/messages -H \"Content-Type: application/json\" -d '{\"messageText\":\"$TEST_MESSAGE\", \"postedBy\":$account_id, \"timePostedEpoch\":$current_timestamp}'")
# message_id=$(echo "$create_message_response" | jq -r '.messageId')

# if [ "$message_id" != "null" ] && [ -n "$message_id" ]; then
#   echo -e "${GREEN}✓ Successfully created message with messageId: $message_id${NC}"
# else
#   echo -e "${RED}✗ Failed to create message${NC}"
#   TESTS_FAILED=$((TESTS_FAILED+1))
# fi

# # =============================================
# # 8. Test Invalid Message Creation
# # =============================================
# print_section "TESTING INVALID MESSAGE CREATION"

# invalid_message_response=$(run_test "Create message with invalid content" "curl -s -X POST $BASE_URL/messages -H \"Content-Type: application/json\" -d '{\"messageText\":\"$INVALID_MESSAGE\", \"postedBy\":$account_id, \"timePostedEpoch\":$current_timestamp}'")

# invalid_user_message_response=$(run_test "Create message with invalid user" "curl -s -X POST $BASE_URL/messages -H \"Content-Type: application/json\" -d '{\"messageText\":\"$TEST_MESSAGE\", \"postedBy\":$INVALID_ID, \"timePostedEpoch\":$current_timestamp}'")

# # =============================================
# # 9. Test Get Message by ID
# # =============================================
# print_section "TESTING MESSAGE RETRIEVAL BY ID"

# get_message_response=$(run_test "Get message by ID" "curl -s -X GET $BASE_URL/messages/$message_id")
# retrieved_message=$(echo "$get_message_response" | jq -r '.messageText')

# if [ "$retrieved_message" == "$TEST_MESSAGE" ]; then
#   echo -e "${GREEN}✓ Successfully retrieved message: $retrieved_message${NC}"
# else
#   echo -e "${RED}✗ Message content mismatch or retrieval failed${NC}"
#   TESTS_FAILED=$((TESTS_FAILED+1))
# fi

# # =============================================
# # 10. Test Get Invalid Message ID
# # =============================================
# print_section "TESTING INVALID MESSAGE RETRIEVAL"

# invalid_get_response=$(run_test "Get message with invalid ID" "curl -s -X GET $BASE_URL/messages/$INVALID_ID")

# # =============================================
# # 11. Test Update Message
# # =============================================
# print_section "TESTING MESSAGE UPDATE"

# update_message_response=$(run_test "Update a message" "curl -s -X PATCH $BASE_URL/messages/$message_id -H \"Content-Type: application/json\" -d '{\"messageText\":\"$TEST_UPDATED_MESSAGE\"}'")

# # Verify update was successful
# verify_update_response=$(run_test "Verify message update" "curl -s -X GET $BASE_URL/messages/$message_id")
# updated_message=$(echo "$verify_update_response" | jq -r '.messageText')

# if [ "$updated_message" == "$TEST_UPDATED_MESSAGE" ]; then
#   echo -e "${GREEN}✓ Successfully verified message update: $updated_message${NC}"
# else
#   echo -e "${RED}✗ Message update verification failed${NC}"
#   TESTS_FAILED=$((TESTS_FAILED+1))
# fi

# # =============================================
# # 12. Test Update Invalid Message
# # =============================================
# print_section "TESTING INVALID MESSAGE UPDATE"

# invalid_update_response=$(run_test "Update message with invalid ID" "curl -s -X PATCH $BASE_URL/messages/$INVALID_ID -H \"Content-Type: application/json\" -d '{\"messageText\":\"$TEST_UPDATED_MESSAGE\"}'")

# # =============================================
# # 13. Test Get All Accounts
# # =============================================
# print_section "TESTING ACCOUNT RETRIEVAL"

# accounts_response=$(run_test "Get all accounts" "curl -s -X GET $BASE_URL/accounts")
# account_count=$(echo "$accounts_response" | jq length)
# echo -e "Found $account_count accounts"

# # =============================================
# # 14. Test Get Messages by User
# # =============================================
# print_section "TESTING USER MESSAGES"

# user_messages_response=$(run_test "Get messages by user" "curl -s -X GET $BASE_URL/accounts/$account_id/messages")
# user_message_count=$(echo "$user_messages_response" | jq length)

# if [ "$user_message_count" -ge 1 ]; then
#   echo -e "${GREEN}✓ Found $user_message_count messages for user $account_id${NC}"
# else
#   echo -e "${RED}✗ No messages found for user or retrieval failed${NC}"
#   TESTS_FAILED=$((TESTS_FAILED+1))
# fi

# # =============================================
# # 15. Test Invalid User Messages
# # =============================================
# print_section "TESTING INVALID USER MESSAGES"

# invalid_user_messages=$(run_test "Get messages for invalid user" "curl -s -X GET $BASE_URL/accounts/$INVALID_ID/messages")

# # =============================================
# # 16. Test Delete Message
# # =============================================
# print_section "TESTING MESSAGE DELETION"

# delete_message_response=$(run_test "Delete message by ID" "curl -s -X DELETE $BASE_URL/messages/$message_id")

# # Verify message was deleted
# verify_delete_response=$(run_test "Verify message deletion" "curl -s -X GET $BASE_URL/messages/$message_id")

# # =============================================
# # 17. Test Delete Invalid Message
# # =============================================
# print_section "TESTING INVALID MESSAGE DELETION"

# invalid_delete_response=$(run_test "Delete message with invalid ID" "curl -s -X DELETE $BASE_URL/messages/$INVALID_ID")

# # =============================================
# # 18. Test Delete User
# # =============================================
# print_section "TESTING USER DELETION"

# delete_user_response=$(run_test "Delete user" "curl -s -X DELETE $BASE_URL/accounts/$account_id")
# delete_result=$(echo "$delete_user_response" | jq -r '.message')

# if [[ "$delete_result" == *"successfully"* ]]; then
#   echo -e "${GREEN}✓ Successfully deleted user: $delete_result${NC}"
# else
#   echo -e "${RED}✗ User deletion failed: $delete_result${NC}"
#   TESTS_FAILED=$((TESTS_FAILED+1))
# fi

# # =============================================
# # 19. Test Delete Invalid User
# # =============================================
# print_section "TESTING INVALID USER DELETION"

# invalid_user_delete=$(run_test "Delete invalid user" "curl -s -X DELETE $BASE_URL/accounts/$INVALID_ID")

# # =============================================
# # Print Test Summary
# # =============================================
# print_summary


# #!/bin/bash

# BASE_URL="http://localhost:8080"

# # Colors for output
# GREEN='\033[0;32m'
# RED='\033[0;31m'
# YELLOW='\033[0;33m'
# NC='\033[0m' # No Color

# # Function to print a section header
# print_section() {
#   echo -e "\n${YELLOW}========================================"
#   echo -e "  ${RED}$1"
#   echo -e "${YELLOW}========================================${NC}"
# }

# # Function to print a section footer
# print_footer() {
#     echo -e "${YELLOW}========================================${NC}"
#     echo -e "${YELLOW}****************************************\n"
# }

# # Function to handle checking of valid JSON response
# check_valid_json() {
#   if echo "$1" | jq . >/dev/null 2>&1; then
#     echo -e "${GREEN}Valid JSON response received.${NC}"
#   else
#     echo -e "${RED}Invalid JSON response!${NC}"
#     exit 1
#   fi
# }

# # Test Home endpoint
# print_section "Testing: Home endpoint"
# home_response=$(curl -s -X GET $BASE_URL/)
# echo -e "Raw Home Response: $home_response"
# check_valid_json "$home_response"
# echo "Home Response: $(echo $home_response | jq .message)"
# print_footer

# # Test Register a new user
# print_section "Testing: Register a new user"
# register_response=$(curl -s -X POST $BASE_URL/register -H "Content-Type: application/json" -d '{"username":"testuser5","password":"password123"}')
# echo -e "Raw Register Response: $register_response"
# check_valid_json "$register_response"
# # Extract account ID from the data field of ApiResponse
# account_id=$(echo "$register_response" | jq -r '.data.accountId // .accountId')
# echo "Successfully registered user with accountId: $account_id"
# print_footer

# # Test Login
# print_section "Testing: Log in a user"
# login_response=$(curl -s -X POST $BASE_URL/login -H "Content-Type: application/json" -d '{"username":"testuser5","password":"password123"}')
# echo -e "Raw Login Response: $login_response"
# check_valid_json "$login_response"
# # Extract username from the data field of ApiResponse
# echo "Login successful. Account: $(echo $login_response | jq '.data.username // .username')"
# print_footer

# # Test Get All Messages
# print_section "Testing: Get all messages"
# messages_response=$(curl -s -X GET $BASE_URL/messages)
# echo -e "Raw Messages Response: $messages_response"
# check_valid_json "$messages_response"
# # Handle both direct array response and ApiResponse data field containing array
# message_count=$(echo "$messages_response" | jq 'if type=="object" and has("data") then .data | length else length end')
# echo "Number of messages: $message_count"
# print_footer

# # Test Create a new message
# print_section "Testing: Create a new message"
# current_timestamp=$(date +%s000)
# create_message_response=$(curl -s -X POST $BASE_URL/messages \
#   -H "Content-Type: application/json" \
#   -d "{\"messageText\":\"This is a test message\", \"postedBy\":$account_id, \"timePostedEpoch\":$current_timestamp}")

# echo -e "Raw Create Message Response: $create_message_response"
# check_valid_json "$create_message_response"

# # Extract message ID handling both direct response and ApiResponse wrapper
# message_id=$(echo "$create_message_response" | jq -r '.data.messageId // .messageId')

# if [ "$message_id" != "null" ] && [ -n "$message_id" ]; then
#     echo "Successfully created message with messageId: $message_id"
# else
#     echo "Failed to create message. Response: $create_message_response"
#     exit 1
# fi  

# # Test Get Message by ID
# print_section "Testing: Get message by ID"
# get_message_response=$(curl -s -X GET $BASE_URL/messages/$message_id)
# echo -e "Raw Get Message Response: $get_message_response"
# check_valid_json "$get_message_response"
# # Extract message text from either direct response or ApiResponse wrapper
# echo "Retrieved message: $(echo $get_message_response | jq '.data.messageText // .messageText')"
# print_footer

# # Test Update Message
# print_section "Testing: Update message"
# update_message_response=$(curl -s -X PATCH $BASE_URL/messages/$message_id -H "Content-Type: application/json" -d '{"messageText":"Updated message text"}')
# echo -e "\nRaw Update Message Response: $update_message_response"
# check_valid_json "$update_message_response"
# # Get rows affected from either direct response or ApiResponse data field
# rows_affected=$(echo $update_message_response | jq '.data // .')
# echo "Updated message, rows affected: $rows_affected"
# print_footer

# # Verify the update by getting the message again
# print_section "Testing: Verify message update"
# updated_message=$(curl -s -X GET $BASE_URL/messages/$message_id)
# echo -e "\nUpdated message content: $(echo $updated_message | jq '.data.messageText // .messageText')"
# print_footer

# # Test Get All Accounts
# print_section "Testing: Get all accounts"
# accounts_response=$(curl -s -X GET $BASE_URL/accounts)
# echo -e "\nRaw Accounts Response: $accounts_response"
# check_valid_json "$accounts_response"
# # Get account count from either direct array or ApiResponse data field containing array
# account_count=$(echo "$accounts_response" | jq 'if type=="object" and has("data") then .data | length else length end')
# echo "Number of accounts: $account_count"
# print_footer

# # Test Get Messages by User
# print_section "Testing: Get messages by user"
# messages_by_user_response=$(curl -s -X GET $BASE_URL/accounts/$account_id/messages)
# echo -e "\nRaw Messages by User Response: $messages_by_user_response"
# check_valid_json "$messages_by_user_response"
# # Get message count from either direct array or ApiResponse data field containing array
# user_message_count=$(echo "$messages_by_user_response" | jq 'if type=="object" and has("data") then .data | length else length end')
# echo "Number of messages by user $account_id: $user_message_count"
# print_footer

# # First delete the messages associated with the user
# print_section "Testing: Delete message by ID"
# delete_message_response=$(curl -s -X DELETE $BASE_URL/messages/$message_id)
# echo -e "\nRaw Delete Message Response: $delete_message_response"
# check_valid_json "$delete_message_response"
# # Get rows affected from either direct response or ApiResponse data field
# rows_affected=$(echo $delete_message_response | jq '.data // .')
# echo "Deleted message with ID: $message_id, rows affected: $rows_affected"
# print_footer

# # Then delete the user
# print_section "Testing: Delete user by accountId"
# delete_user_response=$(curl -s -X DELETE $BASE_URL/accounts/$account_id)
# echo -e "\nRaw Delete User Response: $delete_user_response"
# check_valid_json "$delete_user_response"
# # Extract message from either direct response message field or ApiResponse message field
# echo "Result of deleting user with accountId $account_id: $(echo $delete_user_response | jq '.data.message // .message')"
# print_footer