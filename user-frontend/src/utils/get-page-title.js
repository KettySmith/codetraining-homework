import defaultSettings from '@/settings'

const title = defaultSettings.title || 'CodeTraining-Homework'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
