(function () {
  const el = document.getElementById("slideshow");
  if (!el) return;

  // Replace these with your resort image URLs or local assets paths
  // If using local images, put them in: /assets/img/ and reference like:
  // `${contextPath}/assets/img/slide1.jpg`
  const contextPath = document.body.getAttribute("data-context") || "";
  const images = [
    contextPath + "/assets/img/slide1.jpg",
    contextPath + "/assets/img/slide2.jpg",
    contextPath + "/assets/img/slide3.jpg"
  ];

  let i = 0;

  function setImage() {
    el.style.backgroundImage = `url('${images[i]}')`;
    i = (i + 1) % images.length;
  }

  setImage();
  setInterval(setImage, 4500);
})();