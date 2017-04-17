
//demo1 
(function(){
	const demo1 = document.querySelector('#demo1 .grid')
	const addEl = document.querySelector('#demo1 .add-ele');

	addEl.addEventListener('click', function() {
		let div = document.createElement('div');
		div.classList += 'g-item g-item-animate';
		setTimeout(function() {
			div.classList += ' g-item-show'
		}, 0)
		demo1.appendChild(div);
	})

})()