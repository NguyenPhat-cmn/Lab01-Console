# BaiTap7 - CI/CD Pipeline với GitLab

## Cấu trúc thư mục

```
BaiTap7/
├── .gitlab-ci.yml        # Cấu hình pipeline
├── public/
│   ├── index.html        # Trang web chính
│   ├── styles.css        # CSS
│   └── js/
│       └── main.js       # JavaScript
└── SCREENSHOTS/          # Ảnh minh chứng
    ├── pipeline-success.png
    ├── pipeline-jobs.png
    └── pages-url.png
```

## Pipeline Stages

| Stage  | Job    | Mô tả                          |
|--------|--------|--------------------------------|
| build  | build  | Build project, tạo artifacts   |
| test   | test   | Kiểm tra file tồn tại          |
| deploy | pages  | Deploy lên GitLab Pages        |

## Câu hỏi ôn tập

**Câu 1: CI khác CD chỗ nào?**  
CI tự động build và test mỗi khi push code. CD mở rộng CI bằng cách tự động deploy phiên bản đã pass lên môi trường staging/production.

**Câu 2: Runner là gì? Ai cung cấp?**  
Runner là server/container thực thi các jobs trong pipeline. GitLab.com cung cấp shared runners miễn phí, không cần cài đặt riêng.

**Câu 3: Artifacts dùng để làm gì?**  
Artifacts là file output được jobs tạo ra, có thể chia sẻ giữa các jobs/stages. Ví dụ: thư mục `public/` từ build stage được dùng ở deploy stage.

**Câu 4: Pipeline fail ở stage test, stage deploy có chạy không?**  
Không. Stages chạy tuần tự — nếu 1 stage fail, các stage sau không được thực thi.

**Câu 5: Làm sao để pipeline chỉ chạy trên branch main?**  
Dùng `rules` trong job:
```yaml
rules:
  - if: $CI_COMMIT_BRANCH == $CI_DEFAULT_BRANCH
```

**Câu 6: Muốn chạy pipeline trên mọi branch?**  
Bỏ `rules` hoặc dùng:
```yaml
rules:
  - if: $CI_COMMIT_BRANCH
```

## GitLab Pages URL

```
https://[username].gitlab.io/[project-name]/
```

## Hướng dẫn push

```bash
git add .
git commit -m "Add CI/CD pipeline - BaiTap7"
git push origin main
```
