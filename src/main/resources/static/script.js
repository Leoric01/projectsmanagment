var stripe = Stripe('pk_test_51Q8KOyP3Kv2rNpLzmhieWzuvY06OQqdnS52jKmzzELx6rdtp7jfWkT5q2axOK546oIiQVbZyuHVSDMRn2HKsxwTC00uN39s1yz');
const elements = stripe.elements();

document.addEventListener("DOMContentLoaded", function () {
    const style = {
        base: {
            fontSize: '16px',
            color: '#32325d',
            fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
            fontSmoothing: 'antialiased',
            '::placeholder': {
                color: '#aab7c4',
            },
            ':-webkit-autofill': {
                color: '#32325d',
            },
        },
        invalid: {
            color: '#fa755a',
            iconColor: '#fa755a',
        },
    };

    const cardNumber = elements.create('cardNumber', {style: style});
    cardNumber.mount('#card-number-element');

    const cardExpiry = elements.create('cardExpiry', {style: style});
    cardExpiry.mount('#card-expiry-element');

    const cardCvc = elements.create('cardCvc', {style: style});
    cardCvc.mount('#card-cvc-element');

    const errorElement = document.getElementById('card-errors');
    handleElementChange(cardNumber, errorElement);
    handleElementChange(cardExpiry, errorElement);
    handleElementChange(cardCvc, errorElement);
    const loader = document.getElementById('loader');
    loader.style.display = 'none';

    const form = document.getElementById('payment-form');
    form.addEventListener('submit', function (event) {
        event.preventDefault();
        loader.style.display = 'flex';

        stripe.createToken(cardNumber).then(function (result) {
            loader.style.display = 'none';
            if (result.error) {
                errorElement.textContent = result.error.message;
            } else {
                stripeTokenHandler(result.token);
            }
        });
    });
});

function handleElementChange(element, errorElement) {
    element.on('change', function (event) {
        if (event.error) {
            errorElement.textContent = event.error.message;
        } else {
            errorElement.textContent = '';
        }
    });
}

function stripeTokenHandler(token) {
    let amount = 100;
    fetch('/charge', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            token: token.id,
            amount: amount,
            currency: 'CZK'
        }),
    })
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            if (data.status === 'succeeded') {
                alert('Payment successful!');
            } else {
                alert('Payment failed: ' + data.error);
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}