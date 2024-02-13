document.addEventListener('DOMContentLoaded', function () {
    var addToCartButtons = document.querySelectorAll('.btn-outline-secondary');

// 장바구니 버튼 클릭 이벤트
    addToCartButtons.forEach(function (button) {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            var coffeeName = e.target.getAttribute('data-coffee-name');
            addToCart(coffeeName);
        });
    });
});

function addToCart(coffeeName) {

// Ajax 요청을 사용하여 coffee_name 값을 서버로 전송
    fetch("/auth/add-to-cart", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: coffeeName
    })
        .then(response => {
            if (response.ok) {
                console.log("장바구니에 추가되었습니다.");
                alert("장바구니에 추가되었습니다."); // 팝업창 띄우기
            } else {
                console.log("이미 추가된 상품입니다.");
                alert("이미 추가된 상품입니다."); // 팝창 띄우기
            }
        })
        .catch(error => {
            console.log("에러:", error);
            alert("에러가 발생하였습니다."); // 팝업창 띄우기
        });
}

document.addEventListener('DOMContentLoaded', function () {
    var removeFromRecipeButtons = document.querySelectorAll('.btn-remove-from-recipe');

    removeFromRecipeButtons.forEach(function (button) {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            var recipeId = e.target.getAttribute('data-recipe-id');
            var beanName = e.target.getAttribute('data-bean-name');
            removeFromRecipe(recipeId, beanName);
        });
    });
});

function removeFromRecipe(recipeId, beanName) {
    const formData = new FormData();
    formData.append('recipe_id', recipeId);
    formData.append('bean_name', beanName);

    fetch("/remove-from-recipe", {
        method: "POST",
        body: formData
    })
        .then(response => {
            if (response.ok) {
                console.log("상품이 삭제되었습니다.");
                alert("상품이 삭제되었습니다.");
                location.reload(); // 페이지 새로고침
            } else {
                console.log("상품을 삭제하는 데 실패했습니다.");
                alert("상품을 삭제하는 데 실패했습니다.");
            }
        })
        .catch(error => {
            console.log("에러:", error);
            alert("에러가 발생하였습니다.");
        });
}


//체크박스 처리
function saveCheckboxState() {
    localStorage.setItem("allSelectState", $('#allSelect').prop('checked'));
    localStorage.setItem("iceSelectState", $('#iceSelect').prop('checked'));
    localStorage.setItem("hotSelectState", $('#hotSelect').prop('checked'));
}

function displayCategory() {
    const allSelect = $("#allSelect").prop("checked");
    const iceSelect = $("#iceSelect").prop("checked");
    const hotSelect = $("#hotSelect").prop("checked");

    if (allSelect) {
        $("#ice").show();
        $("#hot").show();
    } else {
        if (iceSelect) {
            $("#ice").show();
        } else {
            $("#ice").hide();
        }

        if (hotSelect) {
            $("#hot").show();
        } else {
            $("#hot").hide();
        }
    }
}

function restoreCheckboxState() {
    const allSelectState = localStorage.getItem("allSelectState");
    const iceSelectState = localStorage.getItem("iceSelectState");
    const hotSelectState = localStorage.getItem("hotSelectState");

    if (allSelectState === "true" || allSelectState === null) {
        $('#allSelect').prop('checked', true);
        $('#iceSelect, #hotSelect').prop('checked', true);
    } else {
        $('#allSelect').prop('checked', false);
        if (iceSelectState === "true" || iceSelectState === null) {
            $('#iceSelect').prop('checked', true);
        } else {
            $('#iceSelect').prop('checked', false);
        }
        if (hotSelectState === "true" || hotSelectState === null) {
            $('#hotSelect').prop('checked', true);
        } else {
            $('#hotSelect').prop('checked', false);
        }
    }
    displayCategory();
}


$(document).ready(function() {
    restoreCheckboxState();

    $('#allSelect').click(function() {
        const isChecked = $(this).prop('checked');
        $('#iceSelect, #hotSelect').prop('checked', isChecked);
        saveCheckboxState();
        displayCategory();
    });

    $('#iceSelect, #hotSelect').click(function() {
        if ($('#iceSelect').prop('checked') && $('#hotSelect').prop('checked')) {
            $('#allSelect').prop('checked', true);
        } else {
            $('#allSelect').prop('checked', false);
        }
        saveCheckboxState();
        displayCategory();
    });
});
