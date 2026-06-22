#!/bin/bash
# ============================================================
# 問い合わせ管理アプリ 起動スクリプト
# ============================================================

set -e

echo "=================================================="
echo "  問い合わせ管理アプリ"
echo "=================================================="
echo ""

# Javaバージョン確認
if ! command -v java &>/dev/null; then
  echo "❌ Java が見つかりません。Java 17 以上をインストールしてください。"
  echo "   https://adoptium.net/"
  exit 1
fi

JAVA_VER=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VER" -lt 17 ] 2>/dev/null; then
  echo "⚠️  Java $JAVA_VER が検出されました。Java 17 以上が必要です。"
  echo "   https://adoptium.net/"
  exit 1
fi
echo "✅ Java $JAVA_VER を検出しました"

# Mavenの確認
if command -v mvn &>/dev/null; then
  MVN_CMD="mvn"
  echo "✅ Maven を検出しました"
elif [ -f "./mvnw" ]; then
  chmod +x ./mvnw
  MVN_CMD="./mvnw"
  echo "✅ Maven Wrapper を使用します"
else
  echo "❌ Maven が見つかりません。以下のいずれかをインストールしてください："
  echo "   brew install maven   (Homebrew)"
  echo "   https://maven.apache.org/"
  exit 1
fi

echo ""
echo "🚀 アプリケーションをビルド・起動します..."
echo "   (初回はMavenの依存関係ダウンロードに数分かかります)"
echo ""

$MVN_CMD spring-boot:run

echo ""
echo "アプリケーションが停止しました。"
