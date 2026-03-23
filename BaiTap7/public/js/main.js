document.addEventListener('DOMContentLoaded', () => {
  const statusText = document.getElementById('status-text');
  const deployTime = document.getElementById('deploy-time');

  statusText.textContent = '✅ Pipeline: PASSED — Site deployed thành công!';
  statusText.style.color = '#4caf50';

  const now = new Date();
  deployTime.textContent = `Deployed at: ${now.toLocaleString('vi-VN')}`;
});
